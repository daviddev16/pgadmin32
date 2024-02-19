-- FUNÇÃO RESPONSÁVEL PELO PGADMIN32 SER CAPAZ DE OBTER O ULTIMO VALOR DE UMA SEQUENCE
CREATE OR REPLACE FUNCTION public.get_sequence_last_value(name) RETURNS int4 AS '
DECLARE
  ls_sequence ALIAS FOR $1;
  lr_record RECORD;
  li_return INT4;
BEGIN
  FOR lr_record IN EXECUTE ''SELECT last_value FROM '' || ls_sequence LOOP
    li_return := lr_record.last_value;
  END LOOP;
  RETURN li_return;
END;'  LANGUAGE 'plpgsql' VOLATILE;