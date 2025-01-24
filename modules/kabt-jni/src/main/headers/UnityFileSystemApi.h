#include <stdint.h>

#if !defined(__cdecl)
#define __cdecl
#endif

struct ObjectInfo {
  uint64_t id;
  uint64_t offset;
  uint64_t size;
  uint32_t typeId;
};

uint32_t __cdecl UFS_Init();
uint32_t __cdecl UFS_Cleanup();
uint32_t __cdecl UFS_MountArchive(const char * path, const char * mountPoint, uint64_t * handle);
uint32_t __cdecl UFS_UnmountArchive(uint64_t handle);
uint32_t __cdecl UFS_GetArchiveNodeCount(uint64_t handle, uint64_t * count);
uint32_t __cdecl UFS_GetArchiveNode(uint64_t handle, uint32_t nodeIndex, char * path, uint32_t pathLen, uint32_t * size, uint32_t * flags);
// UFS_CreateArchive
uint32_t __cdecl UFS_OpenFile(const char * path, uint64_t * handle);
uint32_t __cdecl UFS_ReadFile(uint64_t handle, uint64_t size, char * buffer, uint64_t * actualSize);
uint32_t __cdecl UFS_SeekFile(uint64_t handle, uint64_t offset, char origin, uint64_t * newPosition);
uint32_t __cdecl UFS_GetFileSize(uint64_t handle, uint64_t * size);
uint32_t __cdecl UFS_CloseFile(uint64_t handle);
uint32_t __cdecl UFS_OpenSerializedFile(const char * path, uint64_t * handle);
uint32_t __cdecl UFS_CloseSerializedFile(uint64_t handle);
uint32_t __cdecl UFS_GetExternalReferenceCount(uint64_t handle, uint32_t * count);
uint32_t __cdecl UFS_GetExternalReference(uint64_t handle, uint32_t index, char * path, uint32_t pathLen, char * guid, uint32_t * refType);
uint32_t __cdecl UFS_GetObjectCount(uint64_t handle, uint32_t * count);
uint32_t __cdecl UFS_GetObjectInfo(uint64_t handle, struct ObjectInfo * objectData, uint32_t len);
uint32_t __cdecl UFS_GetTypeTree(uint64_t handle, uint64_t objectId, uint64_t * typeTree);
uint32_t __cdecl UFS_GetRefTypeTypeTree(uint64_t handle, const char * className, const char * namespaceName, const char * assemblyName, uint64_t * typeTree);
uint32_t __cdecl UFS_GetTypeTreeNodeInfo(uint64_t handle, uint32_t node, char * type, uint32_t typeLen, char * name, uint32_t nameLen, uint32_t * offset, uint32_t * size, char * flags, uint32_t * metaFlags, uint32_t * firstChildNode, uint32_t * nextNode);
