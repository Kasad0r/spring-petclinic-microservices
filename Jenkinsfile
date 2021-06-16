pipeline{
 agent any
 tools{
  maven 'Maven 3.5.4' 
  jdk 'OPENJDK-11'
 }
  stages{
    stage('Build'){
     
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
