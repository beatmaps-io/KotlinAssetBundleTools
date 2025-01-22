struct ObjectInfo {
  long long id;
  long long offset;
  long long size;
  int typeId;
};

extern "C" {
    int UFS_Init();
    int UFS_Cleanup();
    int UFS_MountArchive(const char * path, const char * mountPoint, long long * handle);
    int UFS_UnmountArchive(long long handle);
    int UFS_GetArchiveNodeCount(long long handle, long long * count);
    int UFS_GetArchiveNode(long long handle, int nodeIndex, char * path, int pathLen, long * size, int * flags);
    // UFS_CreateArchive
    int UFS_OpenFile(const char * path, long long * handle);
    int UFS_ReadFile(long long handle, long long size, char * buffer, long long * actualSize);
    int UFS_SeekFile(long long handle, long long offset, char origin, long long * newPosition);
    int UFS_GetFileSize(long long handle, long long * size);
    int UFS_CloseFile(long long handle);
    int UFS_OpenSerializedFile(const char * path, long long * handle);
    int UFS_CloseSerializedFile(long long handle);
    int UFS_GetExternalReferenceCount(long long handle, int * count);
    int UFS_GetExternalReference(long long handle, int index, char * path, int pathLen, char * guid, int * refType);
    int UFS_GetObjectCount(long long handle, int * count);
    int UFS_GetObjectInfo(long long handle, struct ObjectInfo * objectData, int len);
    int UFS_GetTypeTree(long long handle, long long objectId, long long * typeTree);
    int UFS_GetRefTypeTypeTree(long long handle, const char * className, const char * namespaceName, const char * assemblyName, long long * typeTree);
    int UFS_GetTypeTreeNodeInfo(long long handle, int node, char * type, int typeLen, char * name, int nameLen, int * offset, int * size, char * flags, int * metaFlags, int * firstChildNode, int * nextNode);
}
