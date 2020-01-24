pipeline {
    agent none
//    environment {
//        APP_NAME = 'Autoplot-dev'
//        AUTH = 'Jeremy Faden'
//        A_URL = 'http://autoplot.org/jnlp/devel/autoplot.jar'
//        REV_DNS = 'org.autoplot'
//    }
    tools {
        ant 'Homebrew_ant'
        jdk 'OSX_JDK_13'
    }
    stages {
        stage('Clean Workspace') {
            agent {
                label 'OSX'
            } 
            steps {
                cleanWs deleteDirs: true
            }
        }
        stage('Build app with ant') {
            agent {
                label 'OSX'
            }
            steps {
                script {
                    git 'git@git.physics.uiowa.edu:dgcrawfo/epublibrarian.git'
                    sh script: '''#!/bin/bash --login
export JAVA_HOME=`java -XshowSettings:properties -version 2>&1| grep java.home | cut -f 7 -d ' '`
env | sort
ant -Ddo.jlink.internal=true clean jar
'''
                }
            }
        }
        stage('Build app with platypus') {
            agent {
                label 'OSX'
            } 
            steps {
                script {
                    sh label: '', returnStatus: true, script: "antBuildScripts/buildPlatypusApp.sh"
                }
            }
        }
        stage('Archive the artifacts') {
            agent {
                label 'OSX'
            } 
            steps {
                script {
                    archiveArtifacts 'packaging/*.zip'
                    //def built = build job: 'Autoplot_app_build', wait: true, parameters: [string(name: 'APP_NAME', value: "${APP_NAME}"), string(name: 'AUTH', value: "${AUTH}"), string(name: 'URL', value: "${A_URL}"), string(name: 'REV_DNS', value: "${REV_DNS}")]
                    //env.upstream_job = "${built.projectName}"
                    //env.upstream_number = "${built.number}"
                }
            }
        }
    }
}