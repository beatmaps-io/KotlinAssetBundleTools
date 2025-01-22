package io.beatmaps.kabt

import io.beatmaps.kabt.base.TypeTreeNodeBase
import io.beatmaps.kabt.external.UFS
import io.beatmaps.kabt.file.UnityFileSystem
import io.beatmaps.kabt.flags.TreeTypeFlags
import io.beatmaps.kabt.flags.TypeTreeMetaFlags

class TypeTreeNode(private val ufs: UnityFileSystem, private val handle: Long, nodeIndex: Int) : TypeTreeNodeBase() {
    override val type: String
    override val name: String
    private val flags: Byte
    private val metaFlags: Int
    override val size: Int
    private val firstChildIndex: Int
    private val nextNodeIndex: Int

    init {
        val info = UFS.getTypeTreeNodeInfo(handle, nodeIndex)

        type = info.nodeType
        name = info.nodeName

        flags = info.flags
        metaFlags = info.metaFlags
        size = info.size
        firstChildIndex = info.firstChild
        nextNodeIndex = info.nextNode
    }

    override val isLeaf = firstChildIndex == 0
    override val isArray = TreeTypeFlags.IsArray.check(flags)
    override val isManagedReferenceRegistry = TreeTypeFlags.IsManagedReferenceRegistry.check(flags)

    override val alignBytes = TypeTreeMetaFlags.AlignBytes.check(metaFlags)
    override val childAlignBytes = TypeTreeMetaFlags.AnyChildUsesAlignBytes.check(metaFlags)

    override val children by lazy {
        if (isLeaf) {
            emptyList()
        } else {
            TypeTreeNode(ufs, handle, firstChildIndex).siblings()
        }
    }

    private fun siblings(): List<TypeTreeNode> =
        if (nextNodeIndex == 0) {
            listOf(this)
        } else {
            val child = TypeTreeNode(ufs, handle, nextNodeIndex)
            listOf(this) + child.siblings()
        }
}
