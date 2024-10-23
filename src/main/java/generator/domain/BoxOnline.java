package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName box_online
 */
@TableName(value ="box_online")
@Data
public class BoxOnline implements Serializable {
    /**
     * 盲盒积分id
     */
    @TableId(value = "box_point_id", type = IdType.AUTO)
    private Long boxPointId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 分钟
     */
    @TableField(value = "minite")
    private Integer minite;

    /**
     * 下一次获得盲盒需要的分钟
     */
    @TableField(value = "next_minite")
    private Integer nextMinite;

    /**
     * 今日已获得盲盒
     */
    @TableField(value = "reward")
    private Integer reward;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private String updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BoxOnline other = (BoxOnline) that;
        return (this.getBoxPointId() == null ? other.getBoxPointId() == null : this.getBoxPointId().equals(other.getBoxPointId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMinite() == null ? other.getMinite() == null : this.getMinite().equals(other.getMinite()))
            && (this.getNextMinite() == null ? other.getNextMinite() == null : this.getNextMinite().equals(other.getNextMinite()))
            && (this.getReward() == null ? other.getReward() == null : this.getReward().equals(other.getReward()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBoxPointId() == null) ? 0 : getBoxPointId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMinite() == null) ? 0 : getMinite().hashCode());
        result = prime * result + ((getNextMinite() == null) ? 0 : getNextMinite().hashCode());
        result = prime * result + ((getReward() == null) ? 0 : getReward().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", boxOnlineId=").append(boxPointId);
        sb.append(", userId=").append(userId);
        sb.append(", minite=").append(minite);
        sb.append(", nextMinite=").append(nextMinite);
        sb.append(", reward=").append(reward);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
