pipeline {
    agent {
        docker {
            // This container comes with JDK 17 and Maven 3.9 pre-installed
            image 'maven:3.6-jdk-8'
            label 'docker'
            // Reuses your local Maven cache so it doesn't re-download the internet every build
            args '-v /root/.m2:/root/.m2' 
        }
    }        

    triggers {
        pollSCM 'H/15 * * * *'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Build the project using Maven
                sh 'mvn clean install'
                junit '**/target/surefire-reports/TEST*.xml'
            }
        }
    }

    post {
        always {
            // Clean up actions
            sh 'echo "Cleaning up..."'
        }

        success {
            // Actions on successful build
            echo 'Build succeeded!'
        }

        failure {
            // Actions on failed build
            echo 'Build failed!'
        }
    }
}