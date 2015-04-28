LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := microhttpd
LOCAL_SRC_FILES := libmicrohttpd/base64.c libmicrohttpd/basicauth.c libmicrohttpd/connection.c \
  libmicrohttpd/daemon.c libmicrohttpd/digestauth.c \
  libmicrohttpd/internal.c libmicrohttpd/md5.c libmicrohttpd/memorypool.c \
  libmicrohttpd/postprocessor.c libmicrohttpd/reason_phrase.c libmicrohttpd/response.c \
  libmicrohttpd/tsearch.c
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog  
LOCAL_C_INCLUDES := $(LOCAL_PATH)/libmicrohttpd
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := Natser
LOCAL_SRC_FILES := NativeService.cpp\
				http-service.c
LOCAL_C_INCLUDES := $(LOCAL_PATH)/libmicrohttpd
LOCAL_SHARED_LIBRARIES := microhttpd
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog				
include $(BUILD_SHARED_LIBRARY)