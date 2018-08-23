var login = new Vue({
    el: '#login',
    data: {
        rules: {
            username: [
                {
                    required: true,
                    message: '请输入用户名',
                    tirgger: 'blur'
                }
            ],
            password: [
                {
                    required: true,
                    message: '请输入密码',
                    tirgger: 'blur'
                },
                {
                    min: 6,
                    max: 20,
                    message: '长度在6到20个字符',
                    tirgger: 'blur'
                }
            ]
        },
        user: {}
    },
    methods: {
        login: function() {
            var _this = this;
            _this.$refs.form.validate(function (valid) {
                if (valid) {
                    _this.doLogin();
                }
                return false;
            });
        },
        doLogin: function() {
            var _this = this;
            var url = contentPath + loginPath;
            var data = {
                username: _this.user.username,
                password: _this.user.password
            }
            $.ajax({
                url: url,
                data: data,
                type: 'POST',
                // contentType:"application/json",
                async: false,
                processData:false,
                success: function(data) {
                    log('data = ' + data);
                    if (data.code == 0) {
                        _this.$message({
                            type: 'success',
                            message: "登陆成功"
                        });
                        // 跳转到首页
                        window.location.href = contentPath;
                    } else {
                        _this.$message({
                            type: 'error',
                            message: "用户名或密码错误"
                        });
                    }
                },
                error: function(e) {
                    _this.$message({
                        type: 'error',
                        message: "系统异常，请联系管理员"
                    });
                }
            })
        }
    }
});