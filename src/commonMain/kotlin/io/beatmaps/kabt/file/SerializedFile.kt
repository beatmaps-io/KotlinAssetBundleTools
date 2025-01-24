package io.beatmaps.kabt.file

import io.beatmaps.kabt.ExternalReference
import io.beatmaps.kabt.type.ExternalReferenceType
import io.beatmaps.kabt.tree.Object
import io.beatmaps.kabt.TypeTreeNode
import io.beatmaps.kabt.base.ISerializedFile
import io.beatmaps.kabt.base.IUnityFileReader
import io.beatmaps.kabt.external.UFS

class SerializedFile(private val ufs: UnityFileSystem, private val reader: IUnityFileReader, private val handle: Long) : ISerializedFile {
    override val objects by lazy {
        val count = UFS.getObjectCount(handle)
        UFS.getObjectInfo(handle, count).map {
            Object(reader, it)
        }
    }

    override val externalReferences by lazy {
        val count = UFS.getExternalReferenceCount(handle)
        List(count) { idx ->
            UFS.getExternalReference(handle, idx).let {
                ExternalReference(it.path, it.guid, ExternalReferenceType.fromInt(it.type))
            }
        }
    }

    override fun getTypeTreeRoot(objId: Long) =
        UFS.getTypeTree(handle, objId).let { typeTreeHandle ->
            cache.getOrPut(typeTreeHandle) {
                TypeTreeNode(ufs, typeTreeHandle, 0)
            }
        }

    override fun getRefTypeTypeTreeRoot(className: String, namespaceName: String, assemblyName: String) =
        UFS.getRefTypeTypeTree(handle, className, namespaceName, assemblyName).let { typeTreeHandle ->
            cache.getOrPut(typeTreeHandle) {
                TypeTreeNode(ufs, typeTreeHandle, 0)
            }
        }

    override fun close() {
        UFS.closeSerializedFile(handle)
    }

    companion object {
        private val cache = mutableMapOf<Long, TypeTreeNode>()
    }
}

