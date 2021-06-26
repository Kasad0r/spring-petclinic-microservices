pipeline {
    agent any
    tools {
        maven 'Maven 3.5.4'
        jdk 'OPENJDK-11'
    }
    stages {
        stage('Clean up docker') {
              environment {
                       sh 'pwd'
               def tagPushWithVersionAndLatest = load('/spring-petclinic-microservices/shared-jenkins-addons/tagPushWithVersionAndLatest.groovy')
            }
            steps {
                sh 'pwd'
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
            environment {
                def version  = readMavenPom().getVersion()
                def tagPushWithVersionAndLatest = load('/shared-jenkins-addons/tagPushWithVersionAndLatest.groovy')
            }
            steps {
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'docker-registry', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                    sh '''docker login --username=$USERNAME --password=$PASSWORD ptclnc.azurecr.io'''
                        tagPushWithVersionAndLatest("spring-petclinic-api-gateway", $version)
                        tagPushWithVersionAndLatest("spring-petclinic-discovery-server", $version)
                        tagPushWithVersionAndLatest("spring-petclinic-config-server", $version)
                        tagPushWithVersionAndLatest("spring-petclinic-visits-service", $version)
                        tagPushWithVersionAndLatest("spring-petclinic-vets-service", $version)
                        tagPushWithVersionAndLatest("spring-petclinic-customers-service", $version)
                        tagPushWithVersionAndLatest("spring-petclinic-admin-server", $version)
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
