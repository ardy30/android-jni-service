/*
 * http_server.c
 *
 *  Created on: 17 Apr, 2015
 *      Author: pranjal.swarup
 */
#include <sys/types.h>
#include <sys/select.h>
#include <sys/socket.h>
#include <microhttpd.h>
#include "log.h"

#define PORT 8888

static int ConnectionCallback(void* cls, struct MHD_Connection* connection,
		const char* url, const char* method, const char* version,
		const char* upload_data, size_t* upload_data_size, void** ptr) {
	int retval = MHD_NO;
	if (!strcmp(method, MHD_HTTP_METHOD_GET)) {
		const char *page = "<html><body>It Works!</body></html>";
		struct MHD_Response *response;
		int ret;
		response = MHD_create_response_from_buffer(strlen(page), (void*) page,
				MHD_RESPMEM_PERSISTENT);
		ret = MHD_queue_response(connection, MHD_HTTP_OK, response);
		MHD_destroy_response(response);
	} else if (!strcmp(method, MHD_HTTP_METHOD_POST)) {
		const char *page = "<html><body>It Works!</body></html>";
		struct MHD_Response *response;
		int ret;
		response = MHD_create_response_from_buffer(strlen(page), (void*) page,
				MHD_RESPMEM_PERSISTENT);
		ret = MHD_queue_response(connection, MHD_HTTP_OK, response);
		MHD_destroy_response(response);
	} else {
		LOGE( "Unexpected method: %s \n", method);
		const char *page = "<html><body>It Works!</body></html>";
		struct MHD_Response *response;
		int ret;
		response = MHD_create_response_from_buffer(strlen(page), (void*) page,
				MHD_RESPMEM_PERSISTENT);
		ret = MHD_queue_response(connection, MHD_HTTP_OK, response);
		MHD_destroy_response(response);
	}

	return (retval);
}

static void PanicCallback(void *cls, const char *file, unsigned int line,
		const char *reason) {
	LOGE( " HTTP server panic: %s (%s, %u)\n", reason, file, line);
}


static int running = 0;
int start_server(int port) {
	LOGE( "Start HTTP server requested \n");
	struct MHD_Daemon* daemon;
	MHD_set_panic_func(&PanicCallback, NULL);
	daemon = MHD_start_daemon(MHD_USE_SELECT_INTERNALLY, port, NULL, NULL,
			&ConnectionCallback, NULL, MHD_OPTION_END);
	if (NULL == daemon) {
		LOGE( "failed to start HTTP server\n");
		return 1;
	}
	running = 1;
	return running;
}

int stop_server(void) {
	LOGE( "Stop HTTP server requested \n");
	struct MHD_Daemon* daemon;
	MHD_stop_daemon(daemon);
	running = 0;
	return running;
}

int is_running(void) {
	return running;
}
