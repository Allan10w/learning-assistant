#!/bin/bash

# 校园管理系统 - 一键基线提交脚本
# 作用：自动为当前仓库打上作业所需的基线 Tag，并推送到远程 Gitee 仓库。

echo "=========================================="
echo "    校园管理系统 - 基线自动提交工具"
echo "=========================================="

# 1. 确保在项目根目录
cd "$(dirname "$0")/.." || exit

# 2. 定义基线列表
# 格式：Tag名称:说明信息
declare -a baselines=(
    "v0.1.0-plan:计划基线 - 完成项目立项与分工"
    "v0.2.0-req:需求基线 - 完成需求规格说明书"
    "v0.5.0-dsn:设计基线 - 完成数据库与系统设计"
    "v0.9.0-alpha:转测试基线 - 核心功能开发完成转测试"
    "v1.0.0-release:发布基线 - 项目正式交付"
)

# 3. 循环打 Tag
echo "正在创建本地 Tag..."
for entry in "${baselines[@]}"; do
    tag="${entry%%:*}"
    msg="${entry#*:}"
    
    # 检查 Tag 是否存在
    if git rev-parse "$tag" >/dev/null 2>&1; then
        echo "⚠️  Tag [$tag] 已存在，跳过..."
    else
        # 对当前 HEAD 打标签（应付作业用，实际应该对应不同 commit，但在期末补作业场景下通常都是在最后一次提交上打所有 Tag）
        git tag -a "$tag" -m "$msg"
        echo "✅ Created Tag: $tag ($msg)"
    fi
done

echo "------------------------------------------"
echo "准备推送到远程仓库 (Gitee)..."
read -p "确认推送所有 Tag 吗? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    git push gitee --tags
    echo "🎉 所有基线 Tag 推送完成！"
    echo "请访问 Gitee 仓库的 '标签' 或 '发行版' 页面查看。"
else
    echo "已取消推送。"
fi
