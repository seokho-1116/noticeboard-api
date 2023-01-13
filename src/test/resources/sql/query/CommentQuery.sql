/*
 댓글을 closure tree 형식으로 저장할 때 댓글 정렬순으로 쿼리 해오기
 ex) 1.
       1.1
           1.1.1
       1.2
           1.2.1
     2.
       2.1
       2.2
 */

/*1step
  조상 댓글의 정보를 가져옴*/
select * from COMMENT as c1
                  join TREE_PATH cc1 on c1.comment_id = cc1.ancestor;

/*2step
  자식 댓글의 정보를 가져옴*/
explain format=json select *
from COMMENT as c1
         join TREE_PATH as cc1 on (cc1.ancestor = c1.comment_id and c1.post_id = 1 and cc1.post_id = 1)
         join COMMENT as c2 on (cc1.descendant = c2.comment_id and c2.post_id = 1);


/*3step
  노드 중에서 바로 아랫자식 즉 깊이가 1인 노드의 자식만 자식 댓글과 조인*/
select *
from COMMENT as c1
         join TREE_PATH as cc1 on (cc1.ancestor = c1.comment_id)
         join COMMENT as c2 on (cc1.descendant = c2.comment_id)
         left outer join TREE_PATH as cc2 on (cc2.descendant = c2.comment_id and cc2.depth = 1);

/*4step
  경로상 자식과 누군가의 자식(자기자신 포함)이기만 하면 조인*/
select *
from COMMENT as c1
         join TREE_PATH as cc1 on (cc1.ancestor = c1.comment_id)
         join COMMENT as c2 on (cc1.descendant = c2.comment_id)
         left outer join TREE_PATH as cc2 on (cc2.descendant = c2.comment_id and cc2.depth = 1)
         join TREE_PATH as breadcrumb on (cc1.descendant = breadcrumb.descendant);

/*5step
  루트 노드에서 시작되서 조인으로 딸려온 아이들만 가져오겠다*/
select *
from COMMENT as c1
         join TREE_PATH as cc1 on (cc1.ancestor = c1.comment_id)
         join COMMENT as c2 on (cc1.descendant = c2.comment_id)
         left outer join TREE_PATH as cc2 on (cc2.descendant = c2.comment_id and cc2.depth = 1)
         join TREE_PATH as breadcrumb on (cc1.descendant = breadcrumb.descendant);
/*5step
  group by로 breadcrumb ancestor column 부모1,부모2... 자기 자신을 묶어버린다.*/
select *, group_concat(breadcrumb.ancestor order by breadcrumb.depth DESC) as breadcrumbs
from COMMENT as c1
         join TREE_PATH as cc1 on (cc1.ancestor = c1.comment_id)
         join COMMENT as c2 on (cc1.descendant = c2.comment_id)
         left outer join TREE_PATH as cc2 on (cc2.descendant = c2.comment_id and cc2.depth = 1)
         join TREE_PATH as breadcrumb on (cc1.descendant = breadcrumb.descendant)
group by cc1.descendant;

/*6step 정렬*/
select *, group_concat(breadcrumb.ancestor order by breadcrumb.depth DESC) as breadcrumbs
from COMMENT as c1
         join TREE_PATH as cc1 on (cc1.ancestor = c1.comment_id)
         join COMMENT as c2 on (cc1.descendant = c2.comment_id)
         left outer join TREE_PATH as cc2 on (cc2.descendant = c2.comment_id and cc2.depth = 1)
         join TREE_PATH as breadcrumb on (cc1.descendant = breadcrumb.descendant)
where c1.parent_id is null
group by cc1.descendant
order by breadcrumbs;

/*7step 최종*/
select c2.*, cc2.ancestor as `_parent`,
       group_concat(breadcrumb.ancestor order by breadcrumb.depth DESC) as breadcrumbs
from COMMENT as c1
         join TREE_PATH as cc1 on (cc1.ancestor = c1.comment_id)
         join COMMENT as c2 on (cc1.descendant = c2.comment_id)
         left outer join TREE_PATH as cc2 on (cc2.descendant = c2.comment_id and cc2.depth = 1)
         join TREE_PATH as breadcrumb on (cc1.descendant = breadcrumb.descendant)
where c1.parent_id is null
group by cc1.descendant
order by breadcrumbs;


explain format = json select c2.*, cc2.ancestor as `_parent`,
       group_concat(breadcrumb.ancestor order by breadcrumb.depth DESC) as breadcrumbs
from COMMENT as c1
         join TREE_PATH cc1 on c1.comment_id = cc1.ancestor and c1.post_id = 1 and cc1.post_id = 1
         join COMMENT as c2 on (cc1.descendant = c2.comment_id and c2.post_id = 1)
         left outer join TREE_PATH as cc2 on (cc2.descendant = c2.comment_id and cc2.depth = 1)
         join TREE_PATH as breadcrumb on (cc1.descendant = breadcrumb.descendant)
group by cc1.descendant
order by breadcrumbs;
