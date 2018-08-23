var permission = new Vue({
    el: '#permission',
    data: {
        permissions: [],
        multipleSelection: [],
        currentPage4: 4,
        input: '',
        options: [{
            value: '选项1',
            label: '黄金糕'
        }, {
            value: '选项2',
            label: '双皮奶'
        }, {
            value: '选项3',
            label: '蚵仔煎'
        }, {
            value: '选项4',
            label: '龙须面'
        }, {
            value: '选项5',
            label: '北京烤鸭'
        }],
        value: '',
        addDialogVisible: false,
        editDialogVisible: false,
        permission: {
            name: '',
            resourceType: '',
            url: '',
            permission: '',
        },
        formLabelWidth: '80px',
        regions: [{
            'name': 'region1'
        }, {
            'name': 'region2'
        }],
        props: {
            label: 'name',
            children: 'zones'
        },
        count: 1,
        dialogTreeVisible: false,
        tree: ''
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
        open: function () {
            this.$alert('这是一段内容', '标题名称', {
                confirmButtonText: '确定',
                callback: function (action) {
                    this.$message({
                        type: 'info',
                        message: '123'
                    });
                }
            });
        },
        showAddForm: function () {
            console.log('show dailog')
            this.dialogFormVisible = true;
        },
        handleCheckChange: function (data, checked, indeterminate) {
            console.log(data, checked, indeterminate);
        },
        handleNodeClick: function (data) {
            console.log(data);
        },
        loadNode: function (node, resolve) {
            if (node.level === 0) {
                return resolve([{
                    name: 'region1'
                }, {
                    name: 'region2'
                }]);
            }
            if (node.level > 3) return resolve([]);

            var hasChild;
            if (node.data.name === 'region1') {
                hasChild = true;
            } else if (node.data.name === 'region2') {
                hasChild = false;
            } else {
                hasChild = Math.random() > 0.5;
            }

            setTimeout(function () {
                var data;
                if (hasChild) {
                    data = [{
                        name: 'zone' + this.count++
                    }, {
                        name: 'zone' + this.count++
                    }];
                } else {
                    data = [];
                }

                resolve(data);
            }, 500);
        },
        showTree: function (event) {
            this.dialogTreeVisible = true;
            // 使当前元素失去焦点
            event.target.blur();
        },
        closeDialog: function (visible) {

        }
    }
});