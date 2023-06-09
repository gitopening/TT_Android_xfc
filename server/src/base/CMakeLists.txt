cmake_minimum_required(VERSION 2.6)
PROJECT(Base)

AUX_SOURCE_DIRECTORY(./ SRC_LIST1)
AUX_SOURCE_DIRECTORY(./pb/protocol SRC_LIST2)
AUX_SOURCE_DIRECTORY(./jsoncpp SRC_LIST3)


SET(PB_INCLUDE_DIR ../protobuf/include)
SET(PB_LIB_DIR ../protobuf/lib)

SET(SLOG_INCLUDE_DIR ../slog)
SET(SLOG_LIB_DIR ../slog)


SET(LIBRARY_OUTPUT_PATH ./)
#SET(EXECUTABLE_OUTPUT_PATH  ./bin)

ADD_DEFINITIONS( -g -W -Wall -D_REENTRANT -D_FILE_OFFSET_BITS=64 -DAC_HAS_INFO
-DAC_HAS_WARNING -DAC_HAS_ERROR -DAC_HAS_CRITICAL -DTIXML_USE_STL
-DAC_HAS_DEBUG -DLINUX_DAEMON -Wno-deprecated)


#设置自己mac os的openssl的路径
if(APPLE)
    SET(OPENSSL_INCLUDE_DIR /usr/local/Cellar/openssl/1.0.2n/include)
    SET(OPENSSL_LIB_DIR /usr/local/Cellar/openssl/1.0.2n/lib)
endif()

INCLUDE_DIRECTORIES(./ ../ ./jsoncpp ${PB_INCLUDE_DIR} ./pb/protocol ${OPENSSL_INCLUDE_DIR} ${SLOG_INCLUDE_DIR})
LINK_DIRECTORIES(./ ${PB_LIB_DIR} ${SLOG_LIB_DIR} ${OPENSSL_LIB_DIR})

ADD_LIBRARY(base STATIC ${SRC_LIST1} ${SRC_LIST2} ${SRC_LIST3})

#ADD_XXXX必须在TARGET_LINK_LIBRARIES前面，否则会报错
#ADD_LIBRARY(${PROJECTNAME} SHARED ${SRC_LIST})
#ADD_EXECUTABLE(${PROJECTNAME} ${SRC_LIST})

TARGET_LINK_LIBRARIES(base pthread slog crypto ssl)
