pipeline{
 agent any
 tools{
  maven 'Maven 3.5.4' 
  jdk 'OPENJDK-11' 
 }
  stages{
   stage('Clean up docker'){
    steps{
     sh '''docker system prune'''
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
    cleanWs()
  }
}
}
