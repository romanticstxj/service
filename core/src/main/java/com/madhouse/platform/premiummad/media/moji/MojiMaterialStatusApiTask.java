package com.madhouse.platform.premiummad.media.moji;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.madhouse.platform.premiummad.media.util.MojiHttpUtil;
import com.madhouse.platform.premiummad.media.util.Sha1;

@Component
public class MojiMaterialStatusApiTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MojiMaterialStatusApiTask.class);

	@Value("${moji.statusUrl}")
	private String getMaterialStatusUrl;

	@Value("${moji.source}")
	private String source;

	@Value("${moji.secret}")
	private String secret;

	@Autowired
	private MojiHttpUtil mojiHttpUtil;

	@Autowired
	private Sha1 sha1;

	public void getStatusResponse() throws Exception {
		LOGGER.info("++++++++++moji get material status begin+++++++++++");
		// TODO
		LOGGER.info("++++++++++moji get material status end+++++++++++");
	}
}
