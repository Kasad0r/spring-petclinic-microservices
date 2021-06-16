pipeline{
 agent any
 tools{
  maven 'Maven bundle' 
  jdk 'OPENJDK-11'
 }
  stages{
    stage('Build'){
      steps{
      sh 'mvnw clean install -P buildDocker'
      }
    }
  }
  post{
    always{
    cleanWs()
    }
  }
}
