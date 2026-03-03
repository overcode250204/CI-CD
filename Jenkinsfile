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
                // Lưu ý: Vì bạn đang chạy Jenkins trên Windows, ta dùng lệnh 'bat'. 
                // Nếu sau này công ty dùng Linux, bạn đổi 'bat' thành 'sh'.
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