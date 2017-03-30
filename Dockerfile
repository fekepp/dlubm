FROM alpine:latest

MAINTAINER Felix Leif Keppmann <felix.leif@keppmann.de>

ENV	S6_VERSION_MAJOR="1" \
	S6_VERSION_MINOR="19" \
	S6_VERSION_PATCH="1" \
	S6_VERSION_BUILD="1"

RUN	apk add --no-cache \
		curl \
		htop \
		nginx \
		openjdk8 \
		raptor2 \
		vim

RUN	curl --silent --show-error --location https://github.com/just-containers/s6-overlay/releases/download/v${S6_VERSION_MAJOR}.${S6_VERSION_MINOR}.${S6_VERSION_PATCH}.${S6_VERSION_BUILD}/s6-overlay-amd64.tar.gz \
		| tar xvzf - -C /

RUN 	mkdir /run/nginx && \
	rm -r /var/www/localhost

COPY	docker /

COPY	build.gradle /root/

RUN	cd /root/ && \
	\
	export URL_PREFIX="https://github.com/fekepp/dlubm/archive/" && \
	export URL_SUFFIX=".tar.gz" && \
	\
	export VERSION=$(cat build.gradle | grep -e "^\s*version = " | sed -e 's/^\s*version = "//'  -e 's/"$//') && \
	export VERSION_MAJOR=$(expr match "${VERSION}" "\([0-9]*\)\.[0-9]*\.[0-9]*") && \
	export VERSION_MINOR=$(expr match "${VERSION}" "[0-9]*\.\([0-9]*\)\.[0-9]*") && \
	export VERSION_PATCH=$(expr match "${VERSION}" "[0-9]*\.[0-9]*\.\([0-9]*\)") && \
	export VERSION_PRERE=$(expr match "${VERSION}" "[0-9]*\.[0-9]*\.[0-9]*-\([a-z]*\)\.[0-9]*") && \
	export VERSION_BUILD=$(expr match "${VERSION}" "[0-9]*\.[0-9]*\.[0-9]*-[a-z]*\.\([0-9]*\)") && \
	\
	if [ "${VERSION_PRERE}" != "" ]; then \
		export URL_MIDDLE="${VERSION_MAJOR}.${VERSION_MINOR}.${VERSION_PATCH}-${VERSION_PRERE}.${VERSION_BUILD}" && \
		export URL="${URL_PREFIX}${URL_MIDDLE}${URL_SUFFIX}"; else \
			export URL_MIDDLE="${VERSION_MAJOR}.${VERSION_MINOR}.${VERSION_PATCH}" && \
			export URL="${URL_PREFIX}${URL_MIDDLE}${URL_SUFFIX}"; fi && \
	\
	export response=$(curl -L --write-out %{http_code} --silent --output /dev/null "${URL}") && \
	echo "Testing > ${response} | ${URL}" && \
	\
	if [ ${response} != "200" ] && [ "${VERSION_PRERE}" != "" ]; then \
		export URL_MIDDLE="${VERSION_MAJOR}.${VERSION_MINOR}.${VERSION_PATCH}-${VERSION_PRERE}" && \
		export URL="${URL_PREFIX}${URL_MIDDLE}${URL_SUFFIX}" && \
		response=$(curl -L --write-out %{http_code} --silent --output /dev/null "${URL}") && \
		echo "Testing > ${response} | ${URL}"; fi && \
	\
	if [ ${response} != "200" ]; then \
		export URL_MIDDLE="${VERSION_MAJOR}.${VERSION_MINOR}-patch" && \
		export URL="${URL_PREFIX}${URL_MIDDLE}${URL_SUFFIX}" && \
		response=$(curl -L --write-out %{http_code} --silent --output /dev/null "${URL}") && \
		echo "Testing > ${response} | ${URL}"; fi && \
	\
	if [ ${response} != "200" ]; then \
		export URL_MIDDLE="${VERSION_MAJOR}-minor" && \
		export URL="${URL_PREFIX}${URL_MIDDLE}${URL_SUFFIX}" && \
		response=$(curl -L --write-out %{http_code} --silent --output /dev/null "${URL}") && \
		echo "Testing > ${response} | ${URL}"; fi && \
	\
	if [ ${response} != "200" ]; then \
		export URL_MIDDLE="master" && \
		export URL="${URL_PREFIX}${URL_MIDDLE}${URL_SUFFIX}" && \
		response=$(curl -L --write-out %{http_code} --silent --output /dev/null "${URL}") && \
		echo "Testing > ${response} | ${URL}"; fi && \
	\
	mkdir source && \
	echo "Downloading > ${URL}" && \
	curl --silent --show-error --location "$URL" -o source.tar.gz && \
	tar --strip-components=1 -xzf source.tar.gz -C source && \
	cd source && \
	./gradlew clean build installDist && \
	\
	\
	mv build/install/dlubm /usr/share/ && \
	rm -rf /var/www/* && \
	curl --silent --show-error --location "http://swat.cse.lehigh.edu/onto/univ-bench.owl" -o "/var/www/univ-bench.owl" && \
	sed -i '/univ-bench\.owl/d' /var/www/univ-bench.owl && \
	\
	\
	echo "DLUBM_VERSION=${VERSION}" >> /var/www/dlubm.log && \
	\
	\
	echo "VERSION=${VERSION}" && \
	echo "VERSION_MAJOR=${VERSION_MAJOR}" && \
	echo "VERSION_MINOR=${VERSION_MINOR}" && \
	echo "VERSION_PATCH=${VERSION_PATCH}" && \
	echo "VERSION_PRERE=${VERSION_PRERE}" && \
	echo "VERSION_BUILD=${VERSION_BUILD}" && \
	\
	echo "URL_PREFIX=${URL_PREFIX}" && \
	echo "URL_MIDDLE=${URL_MIDDLE}" && \
	echo "URL_SUFFIX=${URL_SUFFIX}" && \
	echo "URL=${URL}" && \
	\
	cd /root/ && \
	rm -rf /root/.gradle && \
	rm -rf /root/source && \
	rm -f /root/source.tar.gz && \
	rm -f /root/build.gradle

ENTRYPOINT [ "/init" ]
