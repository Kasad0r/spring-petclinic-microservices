pipeline{
 agent any
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
