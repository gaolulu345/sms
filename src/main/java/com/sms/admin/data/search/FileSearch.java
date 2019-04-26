package com.sms.admin.data.search;

import com.sms.admin.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FileSearch extends Search {

    String adminName;
    String fileKey;
    int[] ids;

    @Override
    public void builData() {
        super.build();
        if (StringUtil.isEmpty(this.adminName) ||
                this.adminName.trim().length() == 0
                ) {
            this.adminName = null;
        }

        if (StringUtil.isEmpty(this.fileKey) ||
                this.fileKey.trim().length() == 0) {
            this.fileKey = null;
        }
    }
}
