// Install multiple packages on an different Machine(Agent)
// Epel-release
// Git
// Ansible
// Terraform
// Packer

properties([
    parameters([
        string(defaultValue: '', description: 'Please enter VM IP Address ', name: 'nodeIP', trim: true)])])


if (nodeIP?.trim()) {
node {
    withCredentials([
        sshUserPrivateKey(credentialsId: 'jenkins-master-ssh-key', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) 
            {
    // some block

     stage('Init') {
        sh 'ssh -o StrictHostKeyChecking=no -i  $SSHKEY  $SSHUSERNAME@${nodeIP} yum install epel-release -y'
    }

    stage('Install git') {
        sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY  $SSHUSERNAME@${nodeIP} yum install git -y'
    }

    stage('Install Java') {
        sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY  $SSHUSERNAME@${nodeIP} yum install java-1.8.0-openjdk-devel  -y'
    }

    stage('Install Ansible') {
        sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY  $SSHUSERNAME@${nodeIP} yum install ansible  -y'
    }
         stage("Install Terraform"){

                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} yum install -y wget unzip'

                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} wget https://releases.hashicorp.com/terraform/0.13.1/terraform_0.13.1_linux_amd64.zip'
                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} unzip terraform_0.13.1_linux_amd64.zip'
                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} mv terraform /usr/bin/terraform-0.13'

                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} wget https://releases.hashicorp.com/terraform/0.12.29/terraform_0.12.29_linux_amd64.zip'
                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} unzip terraform_0.12.29_linux_amd64.zip'
                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} mv terraform /usr/bin/terraform-0.12'

                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} wget https://releases.hashicorp.com/terraform/0.11.14/terraform_0.11.14_linux_amd64.zip'
                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} unzip terraform_0.11.14_linux_amd64.zip'
                sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${nodeIP} mv terraform /usr/bin/terraform-0.11'
            }

    }

            }
}

  else {
    error 'Please enter valid IP address'
}
