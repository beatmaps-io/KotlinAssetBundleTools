#include <stdlib.h>

#include "io_beatmaps_kabt_UFSJNI.h"
#include "UnityFileSystemApi.hpp"

int bufferSize = 255;
char * buffer = (char*) malloc(bufferSize);

void updateHandle(JNIEnv * env, jobject handle, long long value) {
    jclass clazz = env->GetObjectClass(handle);
    jfieldID valueField = env->GetFieldID(clazz, "value", "J");
    env->SetLongField(handle, valueField, value);
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
        env->GetStringUTFChars(path, 0),
        env->GetStringUTFChars(mountPoint, 0),
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
    int ret = UFS_GetArchiveNode(handle, nodeIndex, buffer, bufferSize, &cHandle, &cFlags);

    jclass clazz = env->GetObjectClass(data);

    jstring path = env->NewStringUTF(buffer);
    jfieldID pathField = env->GetFieldID(clazz, "path", "Ljava/lang/String;");
    env->SetObjectField(data, pathField, path);

    jfieldID sizeField = env->GetFieldID(clazz, "size", "I");
    env->SetIntField(data, sizeField, cHandle);

    jfieldID flagsField = env->GetFieldID(clazz, "flags", "I");
    env->SetIntField(data, flagsField, cFlags);

    return ret;
}
