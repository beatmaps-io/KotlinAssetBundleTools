#include <stdlib.h>

#include "io_beatmaps_kabt_UFSJNI.h"
#include "UnityFileSystemApi.h"

#define bufferSize 255
char * buffer;

char* getBuffer() {
    if (buffer == NULL) {
        buffer = (char*) malloc(bufferSize);
    }
    return buffer;
}

void updateHandle(JNIEnv * env, jobject handle, long long value) {
    jclass clazz = (*env)->GetObjectClass(env, handle);
    jfieldID valueField = (*env)->GetFieldID(env, clazz, "value", "J");
    (*env)->SetLongField(env, handle, valueField, value);
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
    char * pathBuffer = getBuffer();
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
