pipeline {
    agent any
    stages {
        stage ('Init') {
            steps {
                sshagent(['1804b215-19f9-4cdf-a1d7-f55ecf6179f4']) {
                    sh '''
                        [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                        ssh-keyscan -H 89.23.106.12 >> ~/.ssh/known_hosts
                    '''
                    sh "ssh root@89.23.106.12 'cd application/backend/ && [ -d /root/application/backend/assistant ] || git clone https://github.com/TkachenkoVitaliy/assistant.git'"
                    sh "ssh root@89.23.106.12 'cd application/backend/assistant/ && git stash && git pull origin'"
                    sh 'scp /opt/maven/conf/settings-git.xml root@89.23.106.12:application/backend/assistant/settings.xml'
                }
            }
        }

        stage ('Deploy') {
            steps {
                sshagent(['1804b215-19f9-4cdf-a1d7-f55ecf6179f4']) {
                    sh "ssh root@89.23.106.12 'cd application/backend/assistant && chmod u+x deploy2.sh && ./deploy2.sh'"
                }
            }
        }
    }
}