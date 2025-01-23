#include <stdlib.h>

#include "io_beatmaps_kabt_UFSJNI.h"
#include "UnityFileSystemApi.h"

#define bufferSize 255
char buff[bufferSize];
char buff2[bufferSize];

char * buffer = buff;
char * buffer2 = buff2;

void updateHandle(JNIEnv * env, jobject handle, long long value) {
    jclass clazz = (*env)->GetObjectClass(env, handle);
    jfieldID valueField = (*env)->GetFieldID(env, clazz, "value", "J");
    (*env)->SetLongField(env, handle, valueField, value);
}

void updateHandleI(JNIEnv * env, jobject handle, int value) {
    jclass clazz = (*env)->GetObjectClass(env, handle);
    jfieldID valueField = (*env)->GetFieldID(env, clazz, "value", "I");
    (*env)->SetIntField(env, handle, valueField, value);
}

///// Start of JNI implementations

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_init
  (JNIEnv * env, jobject thisObject) {
    return UFS_Init();
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_cleanup
  (JNIEnv * env, jobject thisObject) {
    return UFS_Cleanup();
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_mountArchive
  (JNIEnv * env, jobject thisObject, jstring path, jstring mountPoint, jobject handle) {
    long long cHandle = 0;
    int ret = UFS_MountArchive(
        (*env)->GetStringUTFChars(env, path, 0),
        (*env)->GetStringUTFChars(env, mountPoint, 0),
        &cHandle
    );

    updateHandle(env, handle, cHandle);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getArchiveNodeCount
  (JNIEnv * env, jobject thisObject, jlong handle, jobject count) {
    long long cHandle = 0;
    int ret = UFS_GetArchiveNodeCount(handle, &cHandle);

    updateHandle(env, count, cHandle);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getArchiveNode
  (JNIEnv * env, jobject thisObject, jlong handle, jint nodeIndex, jobject data) {
    long cHandle = 0;
    int cFlags = 0;
    char * pathBuffer = buffer;
    int ret = UFS_GetArchiveNode(handle, nodeIndex, pathBuffer, bufferSize, &cHandle, &cFlags);

    jclass clazz = (*env)->GetObjectClass(env, data);

    jstring path = (*env)->NewStringUTF(env, pathBuffer);
    jfieldID pathField = (*env)->GetFieldID(env, clazz, "path", "Ljava/lang/String;");
    (*env)->SetObjectField(env, data, pathField, path);

    jfieldID sizeField = (*env)->GetFieldID(env, clazz, "size", "I");
    (*env)->SetIntField(env, data, sizeField, cHandle);

    jfieldID flagsField = (*env)->GetFieldID(env, clazz, "flags", "I");
    (*env)->SetIntField(env, data, flagsField, cFlags);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_openFile
  (JNIEnv * env, jobject thisObject, jstring path, jobject handle) {
    long long cHandle = 0;
    int ret = UFS_OpenFile(
        (*env)->GetStringUTFChars(env, path, 0),
        &cHandle
    );

    updateHandle(env, handle, cHandle);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getFileSize
  (JNIEnv * env, jobject thisObject, jlong handle, jobject size) {
    long long cSize = 0;
    int ret = UFS_GetFileSize(handle, &cSize);

    updateHandle(env, size, cSize);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_openSerializedFile
  (JNIEnv * env, jobject thisObject, jstring path, jobject handle) {
    long long cHandle = 0;
    int ret = UFS_OpenSerializedFile(
      (*env)->GetStringUTFChars(env, path, 0),
      &cHandle
    );

    updateHandle(env, handle, cHandle);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getExternalReferenceCount
  (JNIEnv * env, jobject thisObject, jlong handle, jobject count) {
    int cCount = 0;
    int ret = UFS_GetExternalReferenceCount(handle, &cCount);

    updateHandleI(env, count, cCount);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getExternalReference
  (JNIEnv * env, jobject thisObject, jlong handle, jint nodeIndex, jobject data) {
    int cHandle = 0;
    char * pathBuffer = buffer;
    char * guidBuffer = buffer2;
    int ret = UFS_GetExternalReference(handle, nodeIndex, pathBuffer, bufferSize, guidBuffer, &cHandle);

    jclass clazz = (*env)->GetObjectClass(env, data);

    jstring path = (*env)->NewStringUTF(env, pathBuffer);
    jfieldID pathField = (*env)->GetFieldID(env, clazz, "path", "Ljava/lang/String;");
    (*env)->SetObjectField(env, data, pathField, path);

    jstring guid = (*env)->NewStringUTF(env, guidBuffer);
    jfieldID guidField = (*env)->GetFieldID(env, clazz, "guid", "Ljava/lang/String;");
    (*env)->SetObjectField(env, data, guidField, guid);

    jfieldID typeField = (*env)->GetFieldID(env, clazz, "type", "I");
    (*env)->SetIntField(env, data, typeField, cHandle);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getObjectCount
  (JNIEnv * env, jobject thisObject, jlong handle, jobject count) {
    int cCount = 0;
    int ret = UFS_GetObjectCount(handle, &cCount);

    updateHandleI(env, count, cCount);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getObjectInfo
  (JNIEnv * env, jobject thisObject, jlong handle, jint len, jobjectArray list) {
    struct ObjectInfo * objBuffer = malloc(len * sizeof(struct ObjectInfo));
    int ret = UFS_GetObjectInfo(handle, objBuffer, len);

    if (len > 0) {
        jobject jInfo = (jobject) (*env)->GetObjectArrayElement(env, list, 0);
        jclass clazz = (*env)->GetObjectClass(env, jInfo);
        jfieldID idField = (*env)->GetFieldID(env, clazz, "id", "J");
        jfieldID offsetField = (*env)->GetFieldID(env, clazz, "offset", "J");
        jfieldID sizeField = (*env)->GetFieldID(env, clazz, "size", "J");
        jfieldID typeIdField = (*env)->GetFieldID(env, clazz, "typeId", "I");

        for (int i = 0; i < len; i++) {
            struct ObjectInfo info = objBuffer[i];
            jobject jInfo = (jobject) (*env)->GetObjectArrayElement(env, list, i);

            (*env)->SetLongField(env, jInfo, idField, info.id);
            (*env)->SetLongField(env, jInfo, offsetField, info.offset);
            (*env)->SetLongField(env, jInfo, sizeField, info.size);
            (*env)->SetIntField(env, jInfo, typeIdField, info.typeId);
        }
    }

    free(objBuffer);
    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_closeFile
  (JNIEnv * env, jobject thisObject, jlong handle) {
    return UFS_CloseFile(handle);
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_closeSerializedFile
  (JNIEnv * env, jobject thisObject, jlong handle) {
    return UFS_CloseSerializedFile(handle);
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getTypeTree
  (JNIEnv * env, jobject thisObject, jlong handle, jlong objectId, jobject typeTree) {
    long long cHandle = 0;
    int ret = UFS_GetTypeTree(handle, objectId, &cHandle);

    updateHandle(env, typeTree, cHandle);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getTypeTreeNodeInfo
  (JNIEnv * env, jobject thisObject, jlong handle, jint node, jobject data) {
    char flags = 0;
    int offset, size, metaFlags, firstChildNode, nextNode = 0;

    int ret = UFS_GetTypeTreeNodeInfo(handle, node, buffer, bufferSize, buffer2, bufferSize, &offset, &size, &flags, &metaFlags, &firstChildNode, &nextNode);

    jclass clazz = (*env)->GetObjectClass(env, data);

    jstring type = (*env)->NewStringUTF(env, buffer);
    jfieldID typeField = (*env)->GetFieldID(env, clazz, "nodeType", "Ljava/lang/String;");
    (*env)->SetObjectField(env, data, typeField, type);

    jstring name = (*env)->NewStringUTF(env, buffer2);
    jfieldID nameField = (*env)->GetFieldID(env, clazz, "nodeName", "Ljava/lang/String;");
    (*env)->SetObjectField(env, data, nameField, name);

    jfieldID offsetField = (*env)->GetFieldID(env, clazz, "offset", "I");
    (*env)->SetIntField(env, data, offsetField, offset);

    jfieldID sizeField = (*env)->GetFieldID(env, clazz, "size", "I");
    (*env)->SetIntField(env, data, sizeField, size);

    jfieldID flagsField = (*env)->GetFieldID(env, clazz, "flags", "B");
    (*env)->SetByteField(env, data, flagsField, flags);

    jfieldID metaFlagsField = (*env)->GetFieldID(env, clazz, "metaFlags", "I");
    (*env)->SetIntField(env, data, metaFlagsField, metaFlags);

    jfieldID firstChildField = (*env)->GetFieldID(env, clazz, "firstChild", "I");
    (*env)->SetIntField(env, data, firstChildField, firstChildNode);

    jfieldID nextNodeField = (*env)->GetFieldID(env, clazz, "nextNode", "I");
    (*env)->SetIntField(env, data, nextNodeField, nextNode);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_seekFile
  (JNIEnv * env, jobject thisObject, jlong handle, jlong offset, jbyte origin, jobject newPosition) {
    long long cHandle = 0;
    int ret = UFS_SeekFile(handle, offset, origin, &cHandle);

    updateHandle(env, newPosition, cHandle);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_readFile
  (JNIEnv * env, jobject thisObject, jlong handle, jlong size, jbyteArray arr, jobject actualSize) {
    jbyte* b = (*env)->GetByteArrayElements(env, arr, NULL);
    long long cHandle = 0;
    int ret = UFS_ReadFile(handle, size, b, &cHandle);

    (*env)->ReleaseByteArrayElements(env, arr, b, 0);
    updateHandle(env, actualSize, cHandle);

    return ret;
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_unmountArchive
  (JNIEnv * env, jobject thisObject, jlong handle) {
    return UFS_UnmountArchive(handle);
}

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getRefTypeTypeTree
  (JNIEnv * env, jobject thisObject, jlong handle, jstring className, jstring namespaceName, jstring assemblyName, jobject typeTree) {
    long long cHandle = 0;
    int ret = UFS_GetRefTypeTypeTree(
        handle,
        (*env)->GetStringUTFChars(env, className, 0),
        (*env)->GetStringUTFChars(env, namespaceName, 0),
        (*env)->GetStringUTFChars(env, assemblyName, 0),
        &cHandle
    );

    updateHandle(env, typeTree, cHandle);

    return ret;
}
