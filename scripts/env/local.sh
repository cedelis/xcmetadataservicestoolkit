export TOMCAT_HOME=$(cat local.properties 2> /dev/null | grep tomcat.dir 2> /dev/null | sed 's/^.*=//' 2> /dev/null)
export TOMCAT_HOME=$(cygpath $TOMCAT_HOME)
export MYSQL_USER=root
export MYSQL_DIR=
export MST_INSTANCE=$(cat local.properties 2> /dev/null | grep mst.root.dir 2> /dev/null | grep -v '^#' | sed 's/^.*=//' 2> /dev/null)
export MST_INSTANCE=$MST_INSTANCE/MST-instances/MetadataServicesToolkit
export MST_INSTANCE=$(cygpath $MST_INSTANCE)
export BASE_DIR="${HOME}"
export JAVA_HOME="C:\dev\java\jdk1.6.0_18"

export provider_name="137 - 175"
export provider_url="http:\/\/128.151.244.137:8080\/OAIToolkit_0.6.1\/oai-request.do"

#export provider_name="132 - 40k"
#export provider_url="http:\/\/128.151.244.132:8080\/OAIToolkit_testDataset_40K\/oai-request.do"

#export provider_name="132 - 1M"
#export provider_url="http:\/\/128.151.244.132:8080\/OAIToolkit_ver0.6.1\/oai-request.do"

#norm/trans service
#export service_id=1
#export service_id_2=2

#dbnorm/dbtrans service
export service_id=99
export service_id_2=199

export begin_comment='\/\*'
export end_comment='\*\/'
export begin_comment_run_harvest=
export end_comment_run_harvest=
export begin_comment_run_norm=${begin_comment}
export end_comment_run_norm=${end_comment}
export begin_comment_run_trans=${begin_comment}
export end_comment_run_trans=${end_comment}
