struct ObjectInfo {
  long long id;
  long long offset;
  long long size;
  int typeId;
};

int __cdecl UFS_Init();
int __cdecl UFS_Cleanup();
int __cdecl UFS_MountArchive(const char * path, const char * mountPoint, long long * handle);
int __cdecl UFS_UnmountArchive(long long handle);
int __cdecl UFS_GetArchiveNodeCount(long long handle, long long * count);
int __cdecl UFS_GetArchiveNode(long long handle, int nodeIndex, char * path, int pathLen, long * size, int * flags);
// UFS_CreateArchive
int __cdecl UFS_OpenFile(const char * path, long long * handle);
int __cdecl UFS_ReadFile(long long handle, long long size, char * buffer, long long * actualSize);
int __cdecl UFS_SeekFile(long long handle, long long offset, char origin, long long * newPosition);
int __cdecl UFS_GetFileSize(long long handle, long long * size);
int __cdecl UFS_CloseFile(long long handle);
int __cdecl UFS_OpenSerializedFile(const char * path, long long * handle);
int __cdecl UFS_CloseSerializedFile(long long handle);
int __cdecl UFS_GetExternalReferenceCount(long long handle, int * count);
int __cdecl UFS_GetExternalReference(long long handle, int index, char * path, int pathLen, char * guid, int * refType);
int __cdecl UFS_GetObjectCount(long long handle, int * count);
int __cdecl UFS_GetObjectInfo(long long handle, struct ObjectInfo * objectData, int len);
int __cdecl UFS_GetTypeTree(long long handle, long long objectId, long long * typeTree);
int __cdecl UFS_GetRefTypeTypeTree(long long handle, const char * className, const char * namespaceName, const char * assemblyName, long long * typeTree);
int __cdecl UFS_GetTypeTreeNodeInfo(long long handle, int node, char * type, int typeLen, char * name, int nameLen, int * offset, int * size, char * flags, int * metaFlags, int * firstChildNode, int * nextNode);
