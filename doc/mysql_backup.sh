#数据库全量备份
#备份路径
BACKUP=/home/weiziplus/backup
#当前的时间作为文件名
DATETIME=`date +%Y%m%d`

echo "===开始备份"

#主机
HOST=127.0.0.1
#用户名
DB_USER=root
#密码
DB_PWD=123456
#备份的数据库名
DATABASE=template

#创建备份的路径
#如果备份的路径文件夹存在，就是用，否则就创建
[ ! -d "$BACKUP/$DATETIME" ] && mkdir -p "$BACKUP/$DATETIME"

#执行mysql的备份数据库的指令
mysqldump -u${DB_USER} -p${DB_PWD} --host=${HOST} ${DATABASE} > $BACKUP/$DATETIME/$DATETIME.sql
#打包备份文件
cd $BACKUP
tar -zcvf $DATETIME.tar.gz $DATETIME
#删除临时目录
rm -rf $BACKUP/$DATETIME

#删除10天前的备份
find $BACKUP -mtime +10 -name "*.tar.gz" -exec rm -rf {} \;
echo "===备份文件成功"
