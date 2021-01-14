properties([
    parameters([
        booleanParam(defaultValue: true, description: 'Do you want to run terrform apply', name: 'terraform_apply'),
        booleanParam(defaultValue: false, description: 'Do you want to run terrform destroy', name: 'terraform_destroy'),
        choice(choices: ['dev', 'qa', 'prod'], description: '', name: 'environment'),
        string(defaultValue: '', description: 'Provide AMI ID', name: 'ami_id', trim: false)
    ])
])
def aws_region_var = ''
if(params.environment == "dev"){
    aws_region_var = "us-east-1"
}
else if(params.environment == "qa"){
    aws_region_var = "us-east-2"
}
else if(params.environment == "prod"){
    aws_region_var = "us-west-2"
}
def tf_vars = """
    s3_bucket = \"terraform-bucket2020\"
    s3_folder_project = \"terraform_ec2\"
    s3_folder_region = \"us-east-1\"
    s3_folder_type = \"terraform-ec2-by-ami-name\"
    s3_tfstate_file = \"infrastructure.tfstate\"
    environment = \"${params.environment}\"
    region      = \"${aws_region_var}\"
    public_key  = \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDU7Sg7nQZfHIAdC82fSm8CCv55wLDGVnjSfslq02jp9XWBNAW32TgdXYAhGPP661fk/ZLIKaQ3JT3/GYdev/uKsgEqCifjavHTrWg/wZ/hKffH2SMQ70PLrk7YsT43/SpUeXIJimAhdoG1OyRwAYNC/PTCsaxAfuYtfRN96dyGiEKDTjX5N4q2EQpOOawbX0htDrrr3DL++XVuCPeVyp0KRypeu34dTpHFdg+kAKHs4djMWHNEv7u0YX2ciETppCk62siiShHTv+4AuJJx0L/eX8hLbf7EXTVMQcL4M0dRtCBESf0Pu/8psm59dy9ZOT4pWI18JTi+qBut8mbJMJI1 mihaelavaranita@Mihaelas-MacBook-Pro.local\"
    ami_id      = \"${params.ami_id}\"
"""
node{
    stage("Pull Repo"){
        cleanWs()
        git url: 'https://github.com/ikambarov/terraform-ec2.git'
    }
    withCredentials([usernamePassword(credentialsId: 'jenkins-aws-access-key', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
        withEnv(["AWS_REGION=${aws_region_var}"]) {
            stage("Terrraform Init"){
                writeFile file: "${params.environment}.tfvars", text: "${tf_vars}"
                sh """
                    bash setenv.sh ${environment}.tfvars
                    terraform-0.13 init
                """
            }        
            if (terraform_apply.toBoolean()) {
                stage("Terraform Apply"){
                    sh """
                        terraform-0.13 apply -var-file ${environment}.tfvars -auto-approve
                    """
                }
            }
            else if (terraform_destroy.toBoolean()) {
                stage("Terraform Destroy"){
                    sh """
                        terraform-0.13 destroy -var-file ${environment}.tfvars -auto-approve
                    """
                }
            }
            else {
                stage("Terraform Plan"){
                    sh """
                        terraform-0.13 plan -var-file ${environment}.tfvars
                    """
                }
            }
        }        
    }    
}