var role = new Vue({
    el: '#role',
    data: {
        // 表单校验规则
        rules: {
            name: [
                // {
                //     required: true,
                //     message: '请输入菜单名称',
                //     tirgger: 'blur'
                // },
                // {
                //     min: 1,
                //     max: 20,
                //     message: '长度在1到20个字符',
                //     tirgger: 'blur'
                // }
            ],
            code: [
                // {
                //     required: true,
                //     message: '请输入菜单名称',
                //     tirgger: 'blur'
                // },
                {
                    validator: function(rule, value, callback) {
                        // log(rule);
                        if (!value || value.trim() == '') {
                            return callback(new Error('请输入角色标识'));
                        }
                        if (!checkLetterAndNumber(value)) {
                            return callback(new Error('只能是字母和数字的组合'));
                        }
                        if (value.length > 20) {
                            return callback(new Error('不能大于20个字符'));
                        }
                        var url = contentPath + rolePath + 'validate-code?id=' + role.role.id + '&code=' + value;
                        ajax(url, null, 'GET', {}).then(function(data) {
                            // 验证不通过
                            if (!data.data) {
                                return callback(new Error('该角色已经存在，请换一个角色标识'));
                            } else {
                                return callback();
                            }
                        });

                    }, trigger: 'blur'
                }
            ],
        },
        // 角色列表
        roles: [],
        // 多选数组
        multipleSelection: [],
        currentPage4: 4,
        input: '',
        value: '',
        // 修改窗口显示/隐藏标志
        editDialogVisible: false,
        // 设置权限窗口显示/隐藏标志
        selectMenuDialogVisible: false,
        // 正在编辑的角色对象
        role: {},
        // loading: true,
        // 树形结构默认属性
        defaultProps: {
            children: 'children',
            label: 'label'
        },
        menuTrees:[], // 菜单列表
        selectedMenus:[], // 已选择菜单列表
    },
    mounted: function() {
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
        cancel: function() {
            var _this = this;
            this.$confirm('您还没有保存，是否退出?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function() {
                _this.editDialogVisible = false;
            }).catch(function() {

            });
        },
        // 打开添加对话框
        goAdd: function() {
            // 清空
            this.role = {}
            this.editDialogVisible = true;
        },
        // 添加
        doAdd: function () {
            var _this = this;
            var url = contentPath + rolePath + 'save';
            // var data = JSON.stringify(this.role);
            // console.log(data)
            ajax(url, this.role, 'POST', null).then(function(data) {
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
            var url = contentPath + rolePath + 'update?id=' + id;
            ajax(url, null, 'GET', null).then(function(data) {
                _this.role = data.data;
                // 打开编辑窗口
                _this.editDialogVisible = true;
            });

        },
        // 更新
        doUpdate: function () {
            var _this = this;
            var url = contentPath + rolePath + 'update';
            // var data = JSON.stringify(this.role);
            // console.log(data);

            ajax(url, this.role, 'POST', null).then(function(data) {
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
        doSave: function() {
            var _this = this;
            this.$refs.form.validate(function(valid) {
                if (valid) {
                    // 存在id则更新
                    if (_this.role.id) {
                        _this.doUpdate();
                    } else { // 不存在id则保存
                        _this.doAdd();
                    }
                }
                return false;
            });
        },
        // 删除
        doDelete: function(id) {
            console.log('delete id = ' + id);
            // 删除前确认
            this.$confirm('确认要删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function() {
                var url = contentPath + rolePath + 'delete?id=' + id;
                ajax(url, null, 'DELETE', null).then(function(data) {
                    role.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    pullData();
                });
            }).catch(function() {
                return;
            });
        },
        /************************ 选择权限 ********************************/
        // 打开设置权限对话框
        goSelectMenu: function(id) {
            log(id);
            var _this = this;
            var url = contentPath + rolePath + 'go-select-menu?id=' + id;
            ajax(url, '', 'GET', {}).then(function(data) {
                console.log(data);
                _this.menuTrees = data.data.menuTrees;
                _this.selectedMenus = data.data.selectedMenus;
                _this.role = data.data.role;
                console.log(_this.selectMenuDialogVisible);
                _this.selectMenuDialogVisible = true;
                console.log(_this.selectMenuDialogVisible);
            });
        },
        // 取消选择
        cancelSelect: function() {
            // 隐藏选择上级菜单对话框
            this.selectMenuDialogVisible = false;
        },
        // 确定选择
        confirmSelect: function() {
            // 获取选中的菜单id
            var menuIds = this.$refs.menuTree.getCheckedKeys();
            var _this = this;
            var url = contentPath + rolePath + '/update-menu?id=' + this.role.id;
            ajax(url, menuIds, 'POST', {"traditional" : true}).then(function(data) {

                // 设置权限成功后，需要调用接口刷新一下权限，这样设置才能马上生效
                var u = contentPath + rolePath + 'refresh-permission';
                ajax(u, null, 'GET', {}).then(function(data) {
                    _this.$message({type:"success", message:"设置权限成功"});
                    _this.selectMenuDialogVisible = false;
                    pullData();
                });

            });

        }
    }
});
// 拉取数据
function  pullData() {
    var url = contentPath + rolePath + '/list';
    ajax(url, '', 'GET', {}).then(function(data) {
        // console.log('success: ' + JSON.stringify(data));
        role.roles = data.data;
        // role.loading = false;
    });
}

