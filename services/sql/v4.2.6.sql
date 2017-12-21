INSERT INTO `premiummad_qa`.`mad_dict_location` (
`id`,
  `level`,
  `parent_code`,
  `code`,
  `name`,
  `domestic`,
  `country_name`,
  `province_name`,
  `city_name`
) 
VALUES
  (
  609,
    1,
    1000000000,
    '1010000000',
    '南极洲',
    0,
    '南极洲',
    '其他',
    '其他'
  ) ;
  
  
UPDATE mad_dict_location SET NAME = '那曲市' , city_name = '那曲市', code = 1156540600 WHERE id = 307;