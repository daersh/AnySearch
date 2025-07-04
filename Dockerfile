# Dockerfile
FROM elasticsearch:8.17.6

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install analysis-nori

COPY zizon-analyzer-1.0-SNAPSHOT.zip /tmp/zizon-analyzer.zip
RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install file:///tmp/zizon-analyzer.zip

# 사용자 사전 파일 복사
# user_dic.txt 파일은 Dockerfile이 있는 디렉토리 또는 그 하위 userdict 폴더에 있어야 합니다.
# Nori 플러그인은 기본적으로 config/userdict_ko.txt를 사용자 사전으로 인식합니다.
#COPY userdict/user_dic.txt /usr/share/elasticsearch/config/userdict_ko.txt

# Elasticsearch 설정 파일 (선택 사항: 필요 시 추가 설정)
# COPY elasticsearch.yml /usr/share/elasticsearch/config/elasticsearch.yml