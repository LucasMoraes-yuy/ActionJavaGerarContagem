import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.service.SWServiceInvoker;

public class ActionButtonGerarOcorrencia implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contexto) throws Exception {

        SWServiceInvoker si = new SWServiceInvoker("http://192.168.104.125:8380", "LUCAS","8a834f0406f99532d9df2d75f549dfb6");
        si.setCriptedPass(Boolean.TRUE);

        Registro[] linhas = contexto.getLinhas();

        // Para pegar campos da linha é necessário que a entidade em questão possua todos os campos definidos na tabela Ex: TGWITT
        for (Registro linha : linhas) {
            String codProd = linha.getCampo("CODPROD").toString();
            String codEndOrigem = linha.getCampo("CODENDORIGEM").toString();
            String nuIvt = contexto.getParam("NUIVT").toString();
            String codUsu = contexto.getParam("CODUSU").toString();

            // contexto.mostraErro("Produto: "+ codProd + "\n"
            //         + "Endereço: " + codEndOrigem + "\n"
            //         + "Nrº Inventário: " + nuIvt + "\n"
            //         + "Usuário: " + codUsu
            // );

            try {
                String body = "\"INVENTARIO\":{\"NUIVT\":{\"$\":"+nuIvt+"},\"BLOQUEADO\":{\"$\":\"N\"},\"CODUSU\":{\"$\":"+codUsu+"},\"CODPROD\":{\"$\":"+codProd+"},\"substituirUsuarios\":{\"$\":false},\"ENDERECO\":{\"CODEND\":[{\"$\":"+codEndOrigem+"}]},\"OPCAO\":{\"$\":0}}";
                si.callAsJson("InventarioSP.geraTarefaContagemEstoque", "mge", body);
                contexto.setMensagemRetorno("Tarefas geradas com sucesso!");
            } catch (Exception e) {
                e.printStackTrace();
                throw new MGEModelException(e);
            }

        }

    }
}