java -Xmx400M -Xms400M -Xlog:codecache+sweep*=trace,class+unload,class+load,os+thread,safepoint,gc*,gc+stringdedup=debug,gc+ergo=trace,gc+age=trace,gc+phases=trace,gc+humongous=trace,jit+compilation=debug:file=/home/mw/thread_dump/mwapp.log:level,tags,time,uptime:filesize=104857600,filecount=5 -jar build/libs/metrics-0.0.1-SNAPSHOT.jar
