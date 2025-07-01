pipeline {
    agent any

    tools {
        maven 'install maven' // Assure-toi que ce nom correspond à ce que tu as défini dans Jenkins
    }

    stages {
        
        stage("Clean up") {
            steps {
                deleteDir() // Nettoyer le workspace avant de commencer
            }
        }

        stage("Checkout SCM") {
            steps {
                checkout scm
            }
        }

         stage("Build project") {
            steps {
                // Navigate to the project directory and build the Maven project
                dir("${env.WORKSPACE}") {
                  sh "mvn clean test -Dspring.profiles.active=test"
              
                }
            }
        }


        
    }
}
