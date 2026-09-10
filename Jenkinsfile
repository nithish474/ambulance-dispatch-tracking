pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out ambulance dispatch project...'
            }
        }

        stage('Compile') {
            steps {
                echo 'Compiling Java source code...'
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running JUnit tests...'
                bat 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging Maven application...'
                bat 'mvn package -DskipTests'
            }
        }

        stage('Run Application') {
            steps {
                echo 'Running ambulance dispatch system...'
                bat 'mvn exec:java'
            }
        }
    }

    post {

        success {
            echo 'Ambulance Dispatch CI/CD Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check the console output.'
        }
    }
}