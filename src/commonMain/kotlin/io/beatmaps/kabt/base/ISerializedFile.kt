package io.beatmaps.kabt.base

import io.beatmaps.kabt.ExternalReference

interface ISerializedFile : AutoCloseable {
    val objects: List<IObject>
    val externalReferences: List<ExternalReference>

    fun getTypeTreeRoot(objId: Long): TypeTreeNodeBase
    fun getRefTypeTypeTreeRoot(className: String, namespaceName: String, assemblyName: String): TypeTreeNodeBase
}
