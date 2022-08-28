package cn.e3.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import cn.e3.common.pojo.SearchItem;
import cn.e3.search.mapper.ItemSearchMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 监听商品添加消息，接收消息manager发来的消息后，
 * 	将对应的商品信息同步到索引库
 * @author
 */
public class ItemAddMessageListener implements MessageListener {
	
	@Autowired
	private ItemSearchMapper itemMapper;
	@Autowired
	private SolrServer solrServer;

	/**
	 * 收到消息执行以下逻辑 同步索引数据
	 * @param message
	 */
	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交/数据库操作
			Thread.sleep(1000);
			//根据商品id查询商品信息
			SearchItem searchItem = itemMapper.getSearchItemById(itemId);
			/**
			 * 创建一个文档对象，并向文档对象中添加域
			 */
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSellPoint());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategoryName());
			//把文档写入索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
