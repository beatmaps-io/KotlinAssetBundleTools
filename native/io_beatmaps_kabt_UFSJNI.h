#include <jni.h>
/* Header for class io_beatmaps_kabt_UFSJNI */

#ifndef _Included_io_beatmaps_kabt_UFSJNI
#define _Included_io_beatmaps_kabt_UFSJNI
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_init
  (JNIEnv *, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_cleanup
  (JNIEnv *, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_mountArchive
  (JNIEnv *, jobject, jstring, jstring, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getArchiveNodeCount
  (JNIEnv *, jobject, jlong, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getArchiveNode
  (JNIEnv *, jobject, jlong, jint, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_openFile
  (JNIEnv *, jobject, jstring, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getFileSize
  (JNIEnv *, jobject, jlong, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_openSerializedFile
  (JNIEnv *, jobject, jstring, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getExternalReferenceCount
  (JNIEnv *, jobject, jlong, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getExternalReference
  (JNIEnv *, jobject, jlong, jint, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getObjectCount
  (JNIEnv *, jobject, jlong, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getObjectInfo
  (JNIEnv *, jobject, jlong, jint, jobjectArray);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_closeFile
  (JNIEnv *, jobject, jlong);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_closeSerializedFile
  (JNIEnv *, jobject, jlong);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getTypeTree
  (JNIEnv *, jobject, jlong, jlong, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getTypeTreeNodeInfo
  (JNIEnv *, jobject, jlong, jint, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_seekFile
  (JNIEnv *, jobject, jlong, jlong, jbyte, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_readFile
  (JNIEnv *, jobject, jlong, jlong, jbyteArray, jobject);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_unmountArchive
  (JNIEnv *, jobject, jlong);

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getRefTypeTypeTree
  (JNIEnv *, jobject, jlong, jstring, jstring, jstring, jobject);

#ifdef __cplusplus
}
#endif
#endif
