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
    }
}
