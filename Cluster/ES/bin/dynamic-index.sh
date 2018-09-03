#!/bin/bash
## 动态信息库person 表的映射

## 删除索引
##curl -XDELETE 's100:9200/dynamic?pretty'  -H 'Content-Type: application/json'
##curl -XDELETE 's100:9200/person?pretty' -H 'Content-Type:application/json'
##curl -XDELETE 's100:9200/car?pretty'  -H 'Content-Type: application/json'

curl -XPUT 's100:9200/dynamic?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
	    "number_of_shards":5,
        "number_of_replicas":1,
        "analysis": {
            "filter": {
                "trigrams_filter": {
                    "type":     "ngram",
                    "min_gram": 2,
                    "max_gram": 20
                }
            },
            "analyzer": {
                "trigrams": {
                    "type":      "custom",
                    "tokenizer": "standard",
                    "filter":   [
                        "lowercase",
                        "trigrams_filter"
                    ]
                },
                "ik": {
                    "tokenizer" : "ik_max_word"
                }
            }
        }
    },
    "mappings": {
         "person": {
         		"properties": {
         			"ftpurl": {
         				"type": "text"
         			},
         			"eyeglasses": {
         				"type": "long"
         			},
         			"gender": {
         				"type": "long"
         			},
         			"haircolor": {
         				"type": "long"
         			},
         			"hairstyle": {
         				"type": "long"
         			},
         			"hat": {
         				"type": "long"
         			},
         			"huzi": {
         				"type": "long"
         			},
         			"tie": {
         				"type": "long"
         			},
         			"ipcid": {
         				"type": "text",
         				"fields": {
         					"keyword": {
         						"type": "keyword"
         					}
         				}
         			},
         			"timeslot": {
         				"type": "long"
         			},
         			"date": {
         				"type": "text"
         			},
         			"exacttime": {
         				"type": "date",
         				"format": "yyyy-MM-dd HH:mm:ss"
         			},
         			"searchtype": {
         				"type": "text"
         			},
         			"clusterid": {
         				"type": "text"
         			},
         			"alarmid": {
         				"type": "long"
         			},
         			"alarmtime": {
                    	"type": "text",
                    	"fields": {
                    		"keyword": {
                    			"type": "keyword"
                    		}
                    	}
                    }
         		}
         	}
        }
    }'

curl -XPUT 's100:9200/dynamic/_settings' -d '{
    "index": {
        "max_result_window": 1000000000
    }
}'

##创建索引--行人
curl -XPUT 's100:9200/person?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
	    "number_of_shards":5,
        "number_of_replicas":1,
        "analysis": {
            "filter": {
                "trigrams_filter": {
                    "type": "ngram",
                    "min_gram": 2,
                    "max_gram": 20
                }
            },
            "analyzer": {
                "trigrams": {
                    "type":      "custom",
                    "tokenizer": "standard",
                    "filter":   [
                        "lowercase",
                        "trigrams_filter"
                    ]
                }
            }
        }
    },
    "mappings": {
         "recognize": {
         		"properties": {
         			"age": {
         				"type": "keyword"
         			},
         			"baby": {
         				"type": "keyword"
         			},
         			"bag": {
         				"type": "keyword"
         			},
         			"bottomColor": {
         				"type": "keyword"
         			},
         			"bottomType": {
         				"type": "keyword"
         			},
         			"hat": {
         				"type": "keyword"
         			},
         			"hair": {
         				"type": "keyword"
         			},
         			"knapsack": {
         				"type": "keyword"
         			},
         			"messengerBag": {
         				"type": "keyword"
         			},
         			"orientation": {
         				"type": "keyword"
         			},
         			"sex": {
         				"type": "keyword"
         			},
         			"shoulderBag": {
         				"type": "keyword"
         			},
         			"umbrella": {
         				"type": "keyword"
         			},
         			"upperColor": {
         				"type": "keyword"
         			},
         			"upperType": {
                    	"type": "keyword"
                    },
					"cType": {
						"type": "keyword"
					},
					"ipcId": {
						"type": "keyword"
					},
					"timeStamp": {
						"type": "keyword"
					},
					"date": {
						"type": "keyword"
					},
					"timeSlot": {
						"type": "integer"
					},
					"startTime": {
						"type": "keyword"
					},
					"surl": {
						"type": "keyword"
					},
					"burl": {
						"type": "keyword"
					},
					"hostname": {
						"type": "keyword"
					},
					"relativePath": {
						"type": "keyword"
					}
         		}
         	}
        }
    }'

curl -XPUT 's100:9200/person/_settings' -d '{
     "index": {
        "max_result_window": 1000000000
    }
}'

## 创建索引--车辆
curl -XPUT 's100:9200/car?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
	    "number_of_shards":5,
        "number_of_replicas":1,
        "analysis": {
            "analyzer": {
                "ik": {
                    "tokenizer" : "ik_smart"
                }
            }
        }
    },
    "mappings": {
         "recognize": {
         		"properties": {
         			"ipcid": {
         				"type": "keyword"
         			},
         			"timestamp": {
         				"type": "keyword"
         			},
         			"date": {
         				"type": "keyword"
         			},

         			"timeslot": {
         				"type": "keyword"
         			},
         			"surl": {
         				"type": "keyword"
         			},
         			"burl": {
         				"type": "keyword"
         			},
         			"relativepath": {
         				"type": "keyword"
         			},
         			"relativepath_big": {
         				"type": "keyword"
         			},
         			"ip": {
         				"type": "keyword"
         			},
         			"hostname": {
         				"type": "keyword"
         			},
         			"vehicle_object_type": {
         				"type": "keyword"
         			},
         			"belt_maindriver": {
         				"type": "keyword"
         			},
         			"belt_codriver": {
         				"type": "keyword"
         			},
					"brand_name": {
         				"type": "text",
         				"analyzer": "ik_smart",
         				"search_analyzer": "ik_smart"
         			},
         			"call_code": {
                    	"type": "keyword"
                    },
					"vehicle_color": {
                    	"type": "keyword"
                    },
					"crash_code": {
                    	"type": "keyword"
                    },
					"danger_code": {
                    	"type": "keyword"
                    },
					"marker_code": {
                    	"type": "keyword"
                    },
					"plate_schelter_code": {
                    	"type": "keyword"
                    },
					"plate_flag_code": {
						"type": "keyword"
					},
					"plate_licence": {
						"type": "keyword"
					},
					"plate_destain_code": {
						"type": "keyword"
					},
					"plate_color_code": {
						"type": "integer"
					},
					"plate_type_code": {
						"type": "keyword"
					},
					"rack_code": {
						"type": "keyword"
					},
					"sunroof_code": {
						"type": "keyword"
					},
					"vehicle_type": {
						"type": "keyword"
					}
         		}
         	}
        }
    }'

curl -XPUT 's100:9200/car/_settings' -d '{
     "index": {
        "max_result_window": 1000000000
    }
}'