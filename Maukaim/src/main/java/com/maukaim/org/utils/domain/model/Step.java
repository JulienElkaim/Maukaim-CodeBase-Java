package com.maukaim.org.utils.domain.model;

import com.maukaim.org.utils.diffable.utils.DiffUtils;
import com.maukaim.org.utils.serializer.utils.JsonHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Step implements Diffable<Step> {
    private String name;
    private String type;
    private Double result;

    @Override
    public DiffResult<Step> diff(Step obj) {
        if(!obj.getType().equals(this.getType())){
            return new DiffBuilder<>(this, obj, ToStringStyle.SIMPLE_STYLE)
                    .append("Type has changed", this.getType(), obj.getType())
                    .append("All content", JsonHelper.serialize(this), JsonHelper.serialize(obj))
                    .build();
        }
        return DiffUtils.getGenericDiff(Step.class, this, obj);
    }
}
