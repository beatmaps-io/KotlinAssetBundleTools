package io.beatmaps.kabt.base

import io.beatmaps.kabt.ExternalType

abstract class TypeTreeNodeBase {
    abstract val type: String
    abstract val name: String
    abstract val size: Int

    val kotlinType by lazy {
        when (type) {
            "int", "SInt32", "TypePtr" -> ExternalType.Int32
            "unsigned int", "UInt32" -> ExternalType.UInt32
            "float" -> ExternalType.Float
            "double" -> ExternalType.Double
            "SInt16" -> ExternalType.Int16
            "UInt16" -> ExternalType.UInt16
            "SInt64" -> ExternalType.Int64
            "FileSize", "UInt64" -> ExternalType.UInt64
            "SInt8" -> ExternalType.Int8
            "UInt8", "char" -> ExternalType.UInt8
            "bool" -> ExternalType.Bool
            else -> null
        }
    }

    abstract val isLeaf: Boolean
    abstract val isArray: Boolean
    val isBasicType by lazy { isLeaf && size > 0 }
    abstract val isManagedReferenceRegistry: Boolean

    abstract val alignBytes: Boolean
    abstract val childAlignBytes: Boolean

    abstract val children: List<TypeTreeNodeBase>
}
