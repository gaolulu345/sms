package com.tp.admin.dao;

import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.TerRatationPicture;
import com.tp.admin.data.entity.TerRatationPictureLog;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.search.TerRatationPictureSearch;

import java.util.List;
import java.util.Map;

public interface TerRatationDao {

    int uploadTerRatationPicture(TerRatationPictureSearch terRatationPictureSearch);

    List<TerRatationPicture> terRatationPictureShow(TerRatationPictureSearch terRatationPictureSearch);

    List<TerRatationPicture> terRatationPictureSearch(TerRatationPictureSearch terRatationPictureSearch);

    int startTerRatationPicture(TerRatationPictureSearch terRatationPictureSearch);

    int terRatationPictureCount(TerRatationPictureSearch terRatationPictureSearch);

    int deleteTerRatationPicture(TerRatationPictureSearch terRatationPictureSearch);

    int addTerRatationLog(TerRatationPictureLog terRatationPictureLog);

    int updateAdPicture(TerRatationPictureSearch terRatationPictureSearch);
}
