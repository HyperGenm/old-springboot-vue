<template>
    <div id="form">
        <dialog-form :formData="form" :formOptions="formOptions"
                     @closeDialog="$emit('closeDialog')" @submit="submit"></dialog-form>
    </div>
</template>

<script>
    export default {
        name: "EditForm",
        components: {
            'dialog-form': () => import('@/components/dialog/form/Index.vue')
        },
        data() {
            return {
                formOptions: [
                    {type: 'input', label: 'ip', prop: 'ipAddress', required: true},
                    {
                        type: 'radio', label: '类型', prop: 'type', required: true, options: [
                            {label: '白名单', value: 'ipListWhite'},
                            {label: '黑名单', value: 'ipListBlack'},
                            {label: '异常ip', value: 'ipListAbnormal'}
                        ]
                    },
                    {type: 'textarea', label: '备注', prop: 'remark'}
                ],
                form: {
                    type: 'ipListWhite'
                }
            }
        },
        methods: {
            submit(form) {
                let that = this;
                this.$axios({
                    url: that.$global.URL.dataDictionary.ipManager.addIp,
                    method: 'post',
                    data: form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialog');
                        that.$emit('renderTable');
                    }
                });
            }
        }
    }
</script>