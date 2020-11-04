#!/bin/bash


build_websocket() {
	cd websocket_server/websocket
	cmake .
	make
	cd ..
}
build_websocket_server() {
	cmake .
	make
	cd ..
}

#check_user
build_websocket
build_websocket_server
