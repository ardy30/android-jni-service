#include <jni.h>
#include <http-service.c>
#include <android/log.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

static jclass myClass;

static int sPort = 0;
void *server_thread(void *threadid)
{
   long tid = (long)threadid;
	__android_log_write(ANDROID_LOG_INFO, "NATIVE_SERIVE_JNI",
			"START_SERVER IN THREAD ID" + tid);
    start_server(sPort);
	pthread_exit(NULL);
}

static long THREAD_ID = 1;

static void start_native_server_thread()
{
   pthread_t threads[THREAD_ID];
   pthread_attr_t attr;
   int rc;
   long t = THREAD_ID++;
   void *status;

   pthread_attr_init(&attr);
   pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);

	 rc = pthread_create(&threads[t], &attr, server_thread, (void *)t);

	 if (rc){
			__android_log_write(ANDROID_LOG_INFO, "NATIVE_SERIVE_JNI",
					"START_SERVER CREATING SERVER THREAD ERROR; return code from pthread_create()");
	 }
	__android_log_write(ANDROID_LOG_INFO, "NATIVE_SERIVE_JNI",
			"START_SERVER END");
}

extern "C" JNIEXPORT void JNICALL Java_com_prs_kw_natser_NatSer_initNative(
		JNIEnv* env, jobject obj){
		jclass tmp = env->GetObjectClass(obj);
		myClass = (jclass)env->NewGlobalRef(tmp);
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_prs_kw_natser_NatSer_startServerNative(
		JNIEnv* env, jobject thiz, jint port) {
	__android_log_write(ANDROID_LOG_INFO, "NATIVE_SERIVE_JNI",
			"START_SERVER BEGIN");
    //jmethodID gOnNativeMessage = env->GetMethodID(env->GetObjectClass(thiz),"onNativeMessage","(Ljava/lang/String;)V");
	jint status = is_running();
	if (status == 1) {
		//env->CallVoidMethod(thiz,gOnNativeMessage,"SERVER FAILED TO START: A SERVER IS ALREADY RUNNING!");
		__android_log_write(ANDROID_LOG_INFO, "NATIVE_SERIVE_JNI",
				"START_SERVER FAILED: SERVER ALREADY RUNNING!");
		return false;
	}

	//env->CallVoidMethod(thiz,gOnNativeMessage,"START_SERVER SETTING PORT TO:" + port);
	sPort = port;
	start_native_server_thread();

	return is_running();
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_prs_kw_natser_NatSer_stopServerNative(
		JNIEnv* env, jobject thiz) {
	__android_log_write(ANDROID_LOG_INFO, "NATIVE_SERIVE_JNI",
			"STOP_SERVER BEGIN");
	static jmethodID gOnNativeMessage = env->GetMethodID(myClass,"onNativeMessage","(Ljava/lang/String;)V");
	//env->CallVoidMethod(thiz,gOnNativeMessage,"SERVER STOPPED!");
	jint status = stop_server();
	__android_log_write(ANDROID_LOG_INFO, "NATIVE_SERIVE_JNI",
			"STOP_SERVER END");
	return (status == 1) ? true : false;
}
