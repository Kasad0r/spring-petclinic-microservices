pipeline {
    agent any
    tools {
        maven 'Maven 3.5.4'
        jdk 'OPENJDK-11'
    }
    stages {
        stage('Clean up docker') {
            steps {
                sh '''docker system prune -f'''
            }
        }
        stage('Pre SonarQube build project') {
            steps {
                sh 'mvn clean install -Dmaven.test.skip=true'
            }
        }
        stage('SonarQube analysis') {
            environment {
                scannerHome = tool 'sonarqube'
            }
            steps {
                withSonarQubeEnv('sonarqube server') {
                    withCredentials([string(credentialsId: 'sonarqube-password', variable: 'PASSWORD')]) {
                        sh '''
                ${scannerHome}/bin/sonar-scanner \
                -D sonar.projectKey=petclinic-service-sonarqube \
                -D sonar.login=admin \
                -D sonar.password=$PASSWORD \
                -D sonar.sources=/var/lib/jenkins/workspace/petclinic-microservices-pipeline/ \
                -D sonar.host.url=http://20.79.176.28:9000/\
                -D sonar.test.exclusions=**/test/**/*.*,**/.mvn/wrapper/*,**/*.java \
                -D sonar.language=java \
                -D sonar.java.source=11 \
                -D sonar.java.binaries=**/target/classes \
                '''
                    }
                }
            }
        }
        stage('Build with Unit tests') {
            steps {
                sh 'mvn clean install -P buildDocker'
            }
        }
        stage('Push Docker images to Registry') {
            steps {
                def pom = readMavenPom file: 'pom.xml'
                def version = writeMavenPom model: pom
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'docker-registry', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                    sh '''docker login --username=$USERNAME --password=$PASSWORD ptclnc.azurecr.io'''
                    sh "docker push  ptclnc.azurecr.io/spring-petclinic-api-gateway:${version}"
                    sh "docker push  ptclnc.azurecr.io/spring-petclinic-discovery-server:${version}"
                    sh "docker push  ptclnc.azurecr.io/spring-petclinic-config-server:${version}"
                    sh "docker push  ptclnc.azurecr.io/spring-petclinic-visits-service:${version}"
                    sh "docker push  ptclnc.azurecr.io/spring-petclinic-vets-service:${version}"
                    sh "docker push  ptclnc.azurecr.io/spring-petclinic-customers-service:${version}"
                    sh "docker push  ptclnc.azurecr.io/spring-petclinic-admin-server:${version}"
                    sh "docker push  ptclnc.azurecr.io/zipkin"
                }
            }
        }
    }
    post {
        always {
            sh '''docker system prune -f'''
            // TO DO
            cleanWs()
        }
    }
}
