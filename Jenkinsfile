pipeline{
 agent any
 tools{
  maven 'Maven 3.5.4' 
  jdk 'OPENJDK-11' 
 }
  stages{
   stage('Clean up docker'){
    steps{
     sh '''docker system prune -f'''  
    }
   }
    stage('Pre SonarQube build project'){
        steps{
              sh 'mvn clean install -Dmaven.test.skip=true'
          }
      }
   stage('SonarQube analysis') {

        environment {
            scannerHome = tool 'sonarqube'
        }
        steps {

            withSonarQubeEnv('sonarqube server') {
                sh '''
                ${scannerHome}/bin/sonar-scanner \
                -D sonar.projectKey=petclinic-service-sonarqube \
                -D sonar.login=admin \
                -D sonar.password=Ep@m2021DeVOps \
                -D  sonar.sources=/var/lib/jenkins/workspace/petclinic-microservices-pipeline/ \
                -D sonar.host.url=http://20.79.176.28:9000/\
                -D sonar.test.exclusions=**/test/**/*.*,**/.mvn/wrapper/*,**/*.java \
                -D sonar.language=java \
                -D sonar.java.source=11 \
                -D sonar.java.binaries=**/target/classes \
                '''

            }
        }
    }
   stage('Build with Unit tests'){
      steps{
      sh 'mvn clean install -P buildDocker'
      }
    }
  }
  post{
    always{
     sh '''docker system prune -f'''
     sh 'push springcommunity/spring-petclinic-api-gateway'
     sh 'push springcommunity/spring-petclinic-discovery-server '
     sh 'push springcommunity/spring-petclinic-config-server  '
     sh 'push springcommunity/spring-petclinic-visits-service '
     sh 'push springcommunity/spring-petclinic-vets-service'
     sh 'push springcommunity/spring-petclinic-customers-service'
     sh 'push springcommunity/spring-petclinic-admin-server'
     // TO DO
    cleanWs()
  }
}
}
