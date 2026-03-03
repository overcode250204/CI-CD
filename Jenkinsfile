pipeline {
    agent any // Cho phép chạy trên bất kỳ máy chủ (node) nào của Jenkins
    
    environment {
        // Tên tài khoản Docker Hub của bạn (nhớ thay đổi nhé)
        DOCKERHUB_USER = "khoinguyen2502" 
        IMAGE_NAME = "java-spring-app"
        
        // Dùng số thứ tự của lần build làm phiên bản (VD: v1, v2, v3...)
        IMAGE_TAG = "v${env.BUILD_ID}" 
        
        // Lấy mật khẩu từ kho an toàn của Jenkins (sẽ cài ở Bước 3)
        DOCKERHUB_CREDS = credentials('dockerhub-credentials') 
    }

    stages {
        stage('1. Kéo Code (Checkout)') {
            steps {
                echo "Đang lấy code mới nhất từ Git..."
                checkout scm
            }
        }

        stage('2. Đóng gói (Build Docker Image)') {
            steps {
                echo "Đang build Docker Image phiên bản ${IMAGE_TAG}..."
                sh "docker build -t ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG} -t ${DOCKERHUB_USER}/${IMAGE_NAME}:latest ."            }
        }

        stage('3. Đẩy lên mạng (Push to Docker Hub)') {
            steps {
                echo "Đang đăng nhập và đẩy lên Docker Hub..."
                sh "echo \$DOCKERHUB_CREDS_PSW | docker login -u \$DOCKERHUB_CREDS_USR --password-stdin"
                
                sh "docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:latest"
            }
        }
        stage('4. Triển khai (Continuous Deployment)') {
            steps {
                echo "Đang cài đặt Helm và gọi Kubernetes cập nhật..."
                
                // Mở két sắt lấy file Kubeconfig ra xài
                withCredentials([file(credentialsId: 'my-kubeconfig', variable: 'KUBECONFIG_FILE')]) {
                    sh '''
                    # 1. Tải công cụ Helm về container Jenkins
                    curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
                    chmod 700 get_helm.sh
                    ./get_helm.sh
                    
                    # 2. Bơm chìa khóa Kubeconfig vào biến môi trường
                    export KUBECONFIG=$KUBECONFIG_FILE
                    
                    # 3. Ra lệnh cho Kubernetes cập nhật Image mới nhất (Đảm bảo đường dẫn ./deploy/jirhub-chart là đúng với repo của bạn)
                    helm upgrade --install jirhub-release ./deploy/jirhub-chart --set image.tag=${IMAGE_TAG}
                    '''
                }
            }
        }
    }

    post {
        always {
            echo "Dọn dẹp rác sau khi làm xong..."
            sh "docker logout || true"
            sh "docker rmi ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG} || true"
        }
        success {
            echo "✅ TUYỆT VỜI! Đã Build và Push thành công!"
        }
        failure {
            echo "❌ CÓ LỖI XẢY RA! Vui lòng kiểm tra lại log."
        }
    }
}