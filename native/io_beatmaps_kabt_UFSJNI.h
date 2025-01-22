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

JNIEXPORT jint JNICALL Java_io_beatmaps_kabt_UFSJNI_getArchiveNode
(JNIEnv *, jobject, jlong, jint, jobject);

#ifdef __cplusplus
}
#endif
#endif
