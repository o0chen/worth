var user = new Vue({
    el: '#user',
    data: {
        // 表单校验规则
        rules: {
            username: [
                {
                    validator: function (rule, value, callback) {
                        // log(rule);
                        if (!value || value.trim() == '') {
                            return callback(new Error('请输入用户名'));
                        }
                        if (!checkLetterAndNumber(value)) {
                            return callback(new Error('只能是字母和数字的组合'));
                        }
                        if (value.length > 20) {
                            return callback(new Error('不能大于20个字符'));
                        }
                        var url = contentPath + userPath + 'validate-username?id=' + user.user.id + '&username=' + value;
                        ajax(url, null, 'GET', {}).then(function(data) {
                            // 验证不通过
                            if (!data.data) {
                                return callback(new Error('该用户名已经存在，请换一个用户名'));
                            } else {
                                return callback();
                            }
                        });

                    }, trigger: 'blur'
                },
            ],
            nickname: [
                {
                    required: true,
                    message: '请输入昵称',
                    tirgger: 'blur'
                },
                {
                    min: 1,
                    max: 20,
                    message: '长度在1到20个字符',
                    tirgger: 'blur'
                }
            ],
            plainPassword: [
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
            ],
            confirmPlainPassword: [
                {
                    validator: function (rule, value, callback) {
                        if (!value || value.trim() == '') {
                            return callback(new Error('请输入确认密码'));
                        }
                        if (value != user.user.plainPassword) {
                            return callback(new Error('两次输入的密码不一致'));
                        }
                        return callback();
                    },
                    tirgger: 'blur'
                }
            ]
        },
        // 用户列表
        users: [],
        // 多选数组
        multipleSelection: [],
        currentPage4: 4,
        input: '',
        value: '',
        // 修改窗口显示/隐藏标志
        editDialogVisible: false,
        // 设置角色窗口显示/隐藏标志
        selectRoleDialogVisible: false,
        // 正在编辑的角色对象
        user: {},
        // loading: true,
        // 树形结构默认属性
        defaultProps: {
            children: 'children',
            label: 'label'
        },
        // 角色列表
        roles: [],
        // 已选择角色列表
        selectedRoles: [],
    },
    mounted: function () {
        // 拉取数据
        pullData();
    },
    methods: {
        handleOpen: function (key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose: function (key, keyPath) {
            console.log(key, keyPath);
        },
        toggleSelection: function (rows) {
            if (rows) {
                for (var i = 0; i < rows.length; i++) {
                    this.$refs.table.toggleRowSelection(rows[i]);
                }
            } else {
                this.$refs.table.clearSelection();
            }
        },
        handleSelectionChange: function (val) {
            this.multipleSelection = val;
        },
        handleSizeChange: function (val) {
            console.log("每页 ${val} 条");
        },
        handleCurrentChange: function (val) {
            console.log("当前页: ${val}");
        },
        // 取消
        cancel: function () {
            var _this = this;
            this.$confirm('您还没有保存，是否退出?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function () {
                _this.editDialogVisible = false;
            }).catch(function () {

            });
        },
        // 打开添加对话框
        goAdd: function () {
            // 清空
            this.user = {
                state: 'ACTIVE'
            }
            this.editDialogVisible = true;
        },
        // 添加
        doAdd: function () {
            var _this = this;
            var url = contentPath + userPath + 'save';
            // var data = JSON.stringify(this.user);
            // console.log(data)
            ajax(url, this.user, 'POST', null).then(function (data) {
                // 关闭窗口
                _this.editDialogVisible = false;
                _this.$message({
                    message: '保存成功',
                    type: 'success'
                });
                pullData();
            });

        },
        // 打开编辑对话框
        goUpdate: function (id) {
            var _this = this;
            console.log('id = ' + id);
            var url = contentPath + userPath + 'update?id=' + id;
            ajax(url, null, 'GET', null).then(function (data) {
                _this.user = data.data;
                // 打开编辑窗口
                _this.editDialogVisible = true;
            });

        },
        // 更新
        doUpdate: function () {
            var _this = this;
            var url = contentPath + userPath + 'update';
            // var data = JSON.stringify(this.user);
            // console.log(data);

            ajax(url, this.user, 'POST', null).then(function (data) {
                console.log('success: ' + data);
                // 关闭窗口
                _this.editDialogVisible = false;
                _this.$message({
                    message: '保存成功',
                    type: 'success'
                });
                pullData();
            });
        },
        // 保存
        doSave: function () {
            var _this = this;
            this.$refs.form.validate(function (valid) {
                if (valid) {
                    // 存在id则更新
                    if (_this.user.id) {
                        _this.doUpdate();
                    } else { // 不存在id则保存
                        _this.doAdd();
                    }
                }
                return false;
            });
        },
        // 删除
        doDelete: function (id) {
            console.log('delete id = ' + id);
            // 删除前确认
            this.$confirm('确认要删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function () {
                var url = contentPath + userPath + 'delete?id=' + id;
                ajax(url, null, 'DELETE', null).then(function (data) {
                    user.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    pullData();
                });
            }).catch(function () {
                return;
            });
        },
        /************************ 设置角色 ********************************/
        // 打开设置角色对话框
        goSelectMenu: function (id) {
            log(id);
            var _this = this;
            var url = contentPath + userPath + 'go-select-role?id=' + id;
            ajax(url, '', 'GET', {}).then(function (data) {
                console.log(data);
                _this.roles = data.data.roles;
                _this.selectedRoles = data.data.selectedRoles;
                _this.user = data.data.user;
                _this.selectRoleDialogVisible = true;
            });
        },
        // 取消选择
        cancelSelect: function () {
            // 隐藏选择上级菜单对话框
            this.selectRoleDialogVisible = false;
        },
        // 确定选择
        confirmSelect: function () {
            // 获取选中的菜单id
            var roleIds = [];
            log(this.selectedRoles);
            for (var i = 0; i < this.selectedRoles.length; i++) {
                roleIds.push(this.selectedRoles[i]);
            }
            log(roleIds);
            var _this = this;
            var url = contentPath + userPath + '/update-role?id=' + this.user.id;
            // return;
            ajax(url, roleIds, 'POST', {"traditional": true}).then(function (data) {
                _this.$message({type: "success", message: "设置角色成功"});
                _this.selectRoleDialogVisible = false;
                pullData();
            });

        }
    }
});
// 拉取数据
function pullData() {
    var url = contentPath + userPath + '/list';
    ajax(url, '', 'GET', {}).then(function (data) {
        console.log('success: ' + JSON.stringify(data));
        user.users = data.data;
        // user.loading = false;
    });
}

