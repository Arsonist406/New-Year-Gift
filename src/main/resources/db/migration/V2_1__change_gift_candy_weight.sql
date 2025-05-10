ALTER TABLE gift_candy_weights
		DROP CONSTRAINT fk_gift_candy_weights_on_candy;

ALTER TABLE gift_candy_weights
		ADD CONSTRAINT fk_gift_candy_weights_on_candy
				FOREIGN KEY (candy_id) REFERENCES candy(id) ON DELETE CASCADE;